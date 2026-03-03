package py.com.cotip.insfrastructure.config.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ApiClientRateLimiter {

    private final ConcurrentMap<UUID, ClientWindow> clientWindows = new ConcurrentHashMap<>();

    public boolean allowRequest(UUID clientId, int requestsPerMinute) {
        long currentMinute = Instant.now().getEpochSecond() / 60;
        ClientWindow window = clientWindows.computeIfAbsent(clientId, ignored -> new ClientWindow(currentMinute));
        return window.allow(currentMinute, requestsPerMinute);
    }

    private static final class ClientWindow {
        private long minute;
        private int count;

        private ClientWindow(long minute) {
            this.minute = minute;
            this.count = 0;
        }

        private synchronized boolean allow(long currentMinute, int requestsPerMinute) {
            if (this.minute != currentMinute) {
                this.minute = currentMinute;
                this.count = 0;
            }

            if (this.count >= requestsPerMinute) {
                return false;
            }

            this.count++;
            return true;
        }
    }
}
