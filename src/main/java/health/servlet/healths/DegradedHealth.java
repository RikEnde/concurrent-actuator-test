package health.servlet.healths;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import static health.servlet.healths.Tool.SLEEPY_TIME;
import static health.servlet.healths.Tool.sleep;

public class DegradedHealth implements HealthIndicator {

    @Override
    public Health health() {
        long n = sleep(SLEEPY_TIME, this);
        return Health.status("DEGRADED").withDetail("sleep", n).build();
    }
}
