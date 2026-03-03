package py.com.cotip.insfrastructure.config.security;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.filter.OncePerRequestFilter;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.insfrastructure.external.cotipdb.model.ApiClientEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static py.com.cotip.insfrastructure.config.CotipConstants.COTIP_API_KEY_HEADER;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String ROLE_API_CLIENT = "ROLE_API_CLIENT";
    private static final int RETRY_AFTER_SECONDS = 60;

    private final ApiClientAuthenticationService authenticationService;
    private final ApiClientRateLimiter rateLimiter;
    private final MeterRegistry meterRegistry;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("${cotip.api.base-path:/cotip}")
    private String protectedBasePath;

    @Value("${cotip.security.api-key.header:" + COTIP_API_KEY_HEADER + "}")
    private String apiKeyHeader;

    @Value("${cotip.security.api-key.enabled:true}")
    private boolean apiKeySecurityEnabled;

    @Value("${cotip.security.api-key.default-requests-per-minute:120}")
    private int defaultRequestsPerMinute;

    public ApiKeyAuthenticationFilter(ApiClientAuthenticationService authenticationService,
                                      ApiClientRateLimiter rateLimiter,
                                      MeterRegistry meterRegistry,
                                      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.authenticationService = authenticationService;
        this.rateLimiter = rateLimiter;
        this.meterRegistry = meterRegistry;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!this.apiKeySecurityEnabled) {
            return true;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return !isProtectedPath(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ApiClientEntity client;
        try {
            client = authenticateClient(request);
        }
        catch (CotipException ex) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, ex);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                client.getClientName(),
                null,
                List.of(new SimpleGrantedAuthority(ROLE_API_CLIENT))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        accepted(client.getClientName());

        try {
            filterChain.doFilter(request, response);
        }
        finally {
            SecurityContextHolder.clearContext();
        }
    }

    private ApiClientEntity authenticateClient(HttpServletRequest request) {
        String rawApiKey = request.getHeader(this.apiKeyHeader);
        if (rawApiKey == null || rawApiKey.isBlank()) {
            rejected("missing_api_key", null);
            throw new CotipException(HttpServletResponse.SC_UNAUTHORIZED,
                    CotipError.API_KEY_REQUIRED.getCode(),
                    "Missing API key in header: " + this.apiKeyHeader,
                    true);
        }

        ApiClientEntity client = authenticationService.authenticate(rawApiKey).orElse(null);
        if (client == null) {
            rejected("invalid_api_key", null);
            throw new CotipException(HttpServletResponse.SC_UNAUTHORIZED,
                    CotipError.API_KEY_INVALID.getCode(),
                    "Invalid API key",
                    true);
        }

        int requestsPerMinute = resolveRequestsPerMinute(client);
        if (!rateLimiter.allowRequest(client.getId(), requestsPerMinute)) {
            rejected("rate_limited", client.getClientName());

            CotipException exception = new CotipException(429,
                    CotipError.API_KEY_RATE_LIMIT_EXCEEDED.getCode(),
                    "Rate limit exceeded for client " + client.getClientName(),
                    true);
            exception.setExtra(Map.of("retryAfterSeconds", RETRY_AFTER_SECONDS));
            throw exception;
        }

        return client;
    }

    private int resolveRequestsPerMinute(ApiClientEntity client) {
        Integer clientLimit = client.getRequestsPerMinute();
        if (clientLimit != null && clientLimit > 0) {
            return clientLimit;
        }
        return Math.max(1, this.defaultRequestsPerMinute);
    }

    private boolean isProtectedPath(HttpServletRequest request) {
        String basePath = normalizePath(this.protectedBasePath);
        String requestPath = requestPath(request);

        return requestPath.equals(basePath) || requestPath.startsWith(basePath + "/");
    }

    private String requestPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();

        if (contextPath != null && !contextPath.isBlank() && requestUri.startsWith(contextPath)) {
            return normalizePath(requestUri.substring(contextPath.length()));
        }

        return normalizePath(requestUri);
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }

        String normalized = path.startsWith("/") ? path : "/" + path;
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            return normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private void accepted(String clientName) {
        meterRegistry.counter(
                "cotip.api.requests.total",
                "result", "accepted",
                "reason", "none",
                "client", clientName
        ).increment();
    }

    private void rejected(String reason, String clientName) {
        String normalizedClient = (clientName == null || clientName.isBlank()) ? "unknown" : clientName;
        meterRegistry.counter(
                "cotip.api.requests.total",
                "result", "rejected",
                "reason", reason,
                "client", normalizedClient
        ).increment();
    }
}
