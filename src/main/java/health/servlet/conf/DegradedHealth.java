package health.servlet.conf;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import reactor.core.publisher.Mono;

import static health.servlet.conf.Tool.sleep;

public class DegradedHealth implements ReactiveHealthIndicator {

  @Override
  public Mono<Health> health() {
    long n = sleep(1000, this);
    return Mono.fromCallable(() -> Health.status("DEGRADED").withDetail("sleep", n).build());
  }
}
