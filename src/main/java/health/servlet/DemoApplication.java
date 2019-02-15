package health.servlet;

import health.servlet.healths.ConcurrentCompositeHealthIndicator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

//import org.springframework.boot.actuate.health.ConcurrentCompositeHealthIndicator;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.SERVLET).run(args);
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

}

