package health.servlet.endpoint;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static health.servlet.conf.Tool.sleep;

@RestController
public class Controller implements HealthIndicator {
  @GetMapping("/")
  public Mono<String> noop() {
    return Mono.fromCallable(() -> "Hello");
  }

  @Override
  public Health health() {
    long n = sleep(1000, this);
    return Health.up().withDetail("sleep", n).build();
  }
}
