package health.servlet;

//import health.servlet.healths.ConcurrentCompositeHealthIndicator;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.health.ConcurrentCompositeHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.boot.actuate.health.Status.DOWN;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.SERVLET).run(args);
    }


    @Bean
    public HealthAggregator healthAggregator() {
        return (healths) -> {
                List<Status> statusCandidates = healths.values().stream().map(Health::getStatus)
                    .collect(Collectors.toList());
                Status status = statusCandidates.contains(Status.UP) ? new Status("NOT ALL BAD") : DOWN;
                return new Health.Builder(status, new LinkedHashMap<>(healths)).build();
        };
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setTaskDecorator(runnable -> () -> {
            long t0 = System.currentTimeMillis();

            runnable.run();
            long t1 = System.currentTimeMillis();
            System.err.printf("Took %d to run on %s\n", t1 - t0, Thread.currentThread().getName());
        });
      return executor;
    }

    @Bean
    public HealthEndpoint healthEndpoint(HealthAggregator healthAggregator,
                                         HealthIndicatorRegistry registry,
                                         ThreadPoolTaskExecutor executor
                                         ) {
        return new HealthEndpoint(
            new ConcurrentCompositeHealthIndicator(healthAggregator, registry, executor, Duration.ofMillis(1100)));
    }

//    @Bean
//    public HealthEndpoint healthEndpointNoTimeout(HealthAggregator healthAggregator,
//                                         HealthIndicatorRegistry registry,
//                                         ThreadPoolTaskExecutor executor
//    ) {
//        return new HealthEndpoint(
//                new ConcurrentCompositeHealthIndicator(healthAggregator, registry, executor));
//    }
}

