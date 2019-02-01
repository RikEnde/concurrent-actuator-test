package health.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  List<HealthIndicator> indicatorList;

  @Autowired
  ThreadPoolTaskExecutor pool;

  @Test
  public void hello() throws Exception {
    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("Hello"));
    ;
  }

  @Test
  public void runsFasterThanAggregateOfAllIndicators() throws Exception {
    long t0 = System.currentTimeMillis();
    this.mockMvc.perform(get("/actuator/health"))
        .andDo(print())
        .andExpect(status().isServiceUnavailable());
    long t1 = System.currentTimeMillis();
    assertThat(t1 - t0).isLessThan(indicatorList.size() * 1000);

    System.err.printf("Took %d ms\n", t1 - t0);
  }

}

