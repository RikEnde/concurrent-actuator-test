package health.servlet.healths;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static health.servlet.healths.Tool.sleep;

@Component
public class UnhealthyHealth implements HealthIndicator {
    @Override
    public Health health() {
        long n = sleep(1000, this);
        return Health.down().withDetail("sleep", n).build();
    }
}