package health.servlet.endpoint;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static health.servlet.healths.Tool.SLEEPY_TIME;
import static health.servlet.healths.Tool.sleep;

@RestController
public class Controller implements HealthIndicator {
    @GetMapping("/")
    public String noop() {
        return "Hello";
    }

    @Override
    public Health health() {
        long n = sleep(SLEEPY_TIME, this);
        return Health.up().withDetail("sleep", n).build();
    }
}
