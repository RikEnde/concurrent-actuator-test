package health.servlet.conf;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import static health.servlet.conf.Tool.sleep;

public class DegradedHealth implements HealthIndicator {

  @Override
  public Health health() {
    long n = sleep(1000, this);
    return Health.status("DEGRADED").withDetail("sleep", n).build();
  }
}
