package health.servlet.conf;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static health.servlet.conf.Tool.sleep;

@Component
public class BadHealth implements HealthIndicator {
  @Override
  public Health health() {
    long n = sleep(1000, this);
    return Health.down().withDetail("sleep", n).build();
  }
}
