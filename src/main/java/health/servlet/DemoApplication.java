package health.servlet;

//import health.servlet.healths.ConcurrentCompositeHealthIndicator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.health.ConcurrentCompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.SERVLET).run(args);
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

