package health.servlet;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
//
//	@Bean
//	public HealthAggregator healthAggregator() {
//		return healths -> {
//			long t0 = System.currentTimeMillis();
//			Health.Builder builder = new Health.Builder();
//			Set<Status> statusMap = new HashSet<>();
//			healths.forEach((key, health) -> {
//				statusMap.add(health.getStatus());
//				builder.withDetail(key, health);
//			});
//			if (statusMap.contains(UP) && statusMap.size() == 1) {
//				builder.status(UP);
//			} else {
//				builder.status(DOWN);
//			}
//			long t1 = System.currentTimeMillis();
//			System.err.printf("Aggregated healths in %d\n", t1 - t0);
//			return builder.build();
//		};
//	}
}

