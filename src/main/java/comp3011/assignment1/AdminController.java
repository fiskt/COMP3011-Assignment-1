package comp3011.assignment1;

import java.time.Duration;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getUptime() {
        Instant serverNow = Instant.now();
        try {
            double uptimeSecodnds = Duration
            .between(serverStarttime, serverNow)
            .toMillis() / 1000.0;
            ServerUptime uptime = new ServerUptime(
                serverStarttime, 
                serverNow, 
                uptimeSecodnds
            );
            return ResponseEntity
                .status(200)
                .body(uptime);
        } catch (Exception e) {
            ErrorResponse uptimeError = new ErrorResponse(
                serverNow,
                500, 
                "Internal Server Error",
                "An unexpected server error occurred.", 
                "/api/v1/admin/uptime"
            );
            return ResponseEntity
                .status(500)
                .body(uptimeError);
        }
        
    }

    @PostMapping("/api/v1/admin/shutdown") 
    public void shutdownServer() {
        return;
    }

    @GetMapping("/api/v1/global/stats")
    public ResponseEntity<?> getGlobalStats() {
        Instant serverNow = Instant.now();
        try {
            GlobalStats globalStats = new GlobalStats(
                tokenCounter.getInputTokens(),
                tokenCounter.getOutputTokens()
            );
            return ResponseEntity
                .status(200)
                .body(globalStats);
        } catch (Exception e) {
            ErrorResponse globalStatsError = new ErrorResponse(
                serverNow, 
                500, 
                "Internal Server Error", 
                "An unexpected server error occurred.", 
                "/api/v1/global/stats"
            );
            return ResponseEntity
                .status(500)
                .body(globalStatsError);
        }
    }

}
