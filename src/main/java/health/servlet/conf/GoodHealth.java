package health.servlet.conf;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static health.servlet.conf.Tool.sleep;

@Component
public class GoodHealth implements ReactiveHealthIndicator {
    @Override
    public Mono<Health> health() {
        long n = sleep(1000, this);
        return Mono.fromCallable(() -> Health.up().withDetail("sleep", n).build());
    }
}
