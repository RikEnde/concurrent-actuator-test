package health.servlet;

import health.servlet.healths.ConcurrentCompositeHealthIndicator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static health.servlet.healths.Tool.SLEEPY_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.actuate.health.Status.DOWN;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompositeHealthIndicatorTests {
    @Autowired
    private HealthAggregator healthAggregator;

    @Autowired
    private HealthIndicatorRegistry registry;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    private HealthEndpoint sequentialEndpoint;

    private HealthEndpoint concurrentEndpoint;

    private HealthEndpoint timeoutEndpoint;

    @Autowired
    List<HealthIndicator> indicatorList;

    @Before
    public void init() {
        sequentialEndpoint = new HealthEndpoint(
                new CompositeHealthIndicator(healthAggregator, registry));
        concurrentEndpoint = new HealthEndpoint(
                new ConcurrentCompositeHealthIndicator(healthAggregator, registry, executor, Duration.ofMillis(SLEEPY_TIME + 100)));

        timeoutEndpoint = new HealthEndpoint(
                new ConcurrentCompositeHealthIndicator(healthAggregator, registry, executor, Duration.ZERO));
    }

    @Test
    public void runtimeSequentialIsAggregateOfAllIndicators() throws Exception {
        long t0 = System.currentTimeMillis();
        Health health = sequentialEndpoint.health();
        long t1 = System.currentTimeMillis();
        assertThat(t1 - t0).isGreaterThanOrEqualTo(indicatorList.size() * SLEEPY_TIME);
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(DOWN);

        System.out.printf("Took %d ms for result %s\n", t1 - t0, health);
    }

    @Test
    public void runtimeConcurrentIsFasterThanAggregateOfAllIndicators() throws Exception {
        long t0 = System.currentTimeMillis();
        Health health = concurrentEndpoint.health();
        long t1 = System.currentTimeMillis();
        assertThat(t1 - t0).isLessThan(indicatorList.size() * SLEEPY_TIME);
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(DOWN);

        System.out.printf("Took %d ms for result %s\n", t1 - t0, health);
    }

    @Test
    public void outputIsIdenticalToCompositeHealthIndicator() throws Exception {
        Health sequential = sequentialEndpoint.health();
        Health concurrent = concurrentEndpoint.health();
        assertThat(sequential).isEqualTo(concurrent);

        System.out.printf("Sequential %s\nConcurrent %s\n", sequential, concurrent);
    }

    @Test
    public void timeoutPerIndividualEndpointWorks() throws Exception {
        Health health = timeoutEndpoint.health();
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(DOWN);

        Map<String, Object> details = health.getDetails();
        Health controller = (Health) details.get("controller");
        Health badHealth = (Health) details.get("badHealth");
        Health goodHealth = (Health) details.get("goodHealth");
        Health healthyHealth = (Health) details.get("healthyHealth");
        Health unhealthyHealth = (Health) details.get("unhealthyHealth");

        assertThat(controller.getStatus()).isEqualTo(DOWN);
        assertThat(badHealth.getStatus()).isEqualTo(DOWN);
        assertThat(goodHealth.getStatus()).isEqualTo(DOWN);
        assertThat(healthyHealth.getStatus()).isEqualTo(DOWN);
        assertThat(unhealthyHealth.getStatus()).isEqualTo(DOWN);

        assertThat(controller.getDetails().get("error")).isEqualTo("java.lang.IllegalStateException: Health check timed out after PT0S");
        assertThat(badHealth.getDetails().get("error")).isEqualTo("java.lang.IllegalStateException: Health check timed out after PT0S");
        assertThat(goodHealth.getDetails().get("error")).isEqualTo("java.lang.IllegalStateException: Health check timed out after PT0S");
        assertThat(healthyHealth.getDetails().get("error")).isEqualTo("java.lang.IllegalStateException: Health check timed out after PT0S");
        assertThat(unhealthyHealth.getDetails().get("error")).isEqualTo("java.lang.IllegalStateException: Health check timed out after PT0S");
        System.out.printf("Timeout %s\n", health);
    }
}

