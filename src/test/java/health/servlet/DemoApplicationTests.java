package health.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest
public class DemoApplicationTests {
	@Autowired
	private WebTestClient webClient;

	@Test
	public void get() throws Exception {
		webClient.get().uri("/")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody().equals("Hello");
	}

	@Test
	public void health() throws Exception {
		webClient.get().uri("/actuator/health")
				.exchange()
				.expectStatus().is5xxServerError();
	}


}

