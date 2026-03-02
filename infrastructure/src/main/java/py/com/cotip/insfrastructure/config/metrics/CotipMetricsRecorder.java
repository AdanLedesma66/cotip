package py.com.cotip.insfrastructure.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CotipMetricsRecorder implements ApplicationMetricsPort {

    private final MeterRegistry meterRegistry;
    private final Map<String, AtomicReference<Double>> freshnessByProvider = new ConcurrentHashMap<>();

    public CotipMetricsRecorder(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void recordScrapeSuccess(ProviderType provider, long startNanos) {
        timer("cotip.scrape.duration", provider.name()).record(System.nanoTime() - startNanos, TimeUnit.NANOSECONDS);
        counter("cotip.scrape.success.total", provider.name(), null).increment();
    }

    @Override
    public void recordScrapeFailure(ProviderType provider, String errorType, long startNanos) {
        timer("cotip.scrape.duration", provider.name()).record(System.nanoTime() - startNanos, TimeUnit.NANOSECONDS);
        counter("cotip.scrape.failure.total", provider.name(), errorType).increment();
    }

    @Override
    public void recordIngested(ProviderType provider, int amount) {
        counter("cotip.rates.ingested.total", provider.name(), null).increment(amount);
    }

    @Override
    public void recordFreshness(ProviderType provider, List<ExchangeRateBO> rates) {
        if (rates == null || rates.isEmpty()) {
            return;
        }

        OffsetDateTime latest = rates.stream()
                .map(ExchangeRateBO::getLastUpdated)
                .filter(java.util.Objects::nonNull)
                .max(OffsetDateTime::compareTo)
                .orElse(null);

        if (latest == null) {
            return;
        }

        long freshnessSeconds = Math.max(0, Duration.between(latest, OffsetDateTime.now()).getSeconds());
        AtomicReference<Double> holder = freshnessByProvider.computeIfAbsent(provider.name(), key -> {
            AtomicReference<Double> reference = new AtomicReference<>(0.0);
            Gauge.builder("cotip.data.freshness.seconds", reference, AtomicReference::get)
                    .description("Freshness in seconds by provider")
                    .tag("provider", provider.name())
                    .register(meterRegistry);
            return reference;
        });

        holder.set((double) freshnessSeconds);
    }

    private Timer timer(String name, String provider) {
        return Timer.builder(name)
                .description("Scrape duration by provider")
                .tag("provider", provider)
                .register(meterRegistry);
    }

    private Counter counter(String name, String provider, String errorType) {
        Counter.Builder builder = Counter.builder(name)
                .tag("provider", provider);

        if (errorType != null && !errorType.isBlank()) {
            builder.tag("error_type", errorType);
        }

        return builder.register(meterRegistry);
    }
}
