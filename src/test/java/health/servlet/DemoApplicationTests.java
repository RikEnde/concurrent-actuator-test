package health.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebFluxTest
public class DemoApplicationTests {
	@Autowired
	private WebTestClient webClient;

	@Autowired
	List<ReactiveHealthIndicator> indicatorList;

	@Test
	public void get() throws Exception {
		webClient.get().uri("/")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody().equals("Hello");
	}

	/** Why this not work? */
	@Test
	public void health() throws Exception {
		long t0 = System.currentTimeMillis();
		webClient.get().uri("/actuator/health")
				.exchange()
				.expectStatus().is5xxServerError();
		long t1 = System.currentTimeMillis();
		assertThat(t1 - t0).isLessThan(indicatorList.size() * 1000);

		System.err.printf("Took %d ms\n", t1 - t0);
	}


}

