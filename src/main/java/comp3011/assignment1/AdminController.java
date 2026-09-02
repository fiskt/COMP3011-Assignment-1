package comp3011.assignment1;

import java.time.Duration;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    private final TokenCounter tokenCounter;
    public AdminController(TokenCounter tokenCounter) {
        this.tokenCounter = tokenCounter;
    }
    private final Instant serverStarttime = Instant.now();

    private record ServerUptime(
        Instant utcServerStart, 
        Instant utcNow,
        double serverUptimeSeconds
    ) {}

    private record GlobalStats(
        long inputTokens,
        long outputTokens
    ) {}

    private record ErrorResponse(
        Instant timestamp,
        int status,
        String error, 
        String message, 
        String path
    ) {}

    @GetMapping("/api/v1/admin/uptime")
    public ServerUptime getUptime() {
        Instant serverNow = Instant.now();
        double uptimeSecodnds = Duration
            .between(serverStarttime, serverNow)
            .toMillis() / 1000;
        ServerUptime uptime = new ServerUptime(
            serverStarttime, 
            serverNow, 
            uptimeSecodnds
        );
        return uptime;
    }

    @PostMapping("/api/v1/admin/shutdown") 
    public void shutdownServer() {
        return;
    }

    @GetMapping("/api/v1/global/stats")
    public GlobalStats getGlobalStats() {
        GlobalStats globalStats = new GlobalStats(
            tokenCounter.getInputTokens(),
            tokenCounter.getOutputTokens()
        );
        return globalStats;
    }

}
