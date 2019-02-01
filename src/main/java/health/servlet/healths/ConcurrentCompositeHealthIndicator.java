/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package health.servlet.healths;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * {@link CompositeHealthIndicator} that gathers health indications from all registered
 * delegates concurrently.
 *
 * @author Rik vd Ende
 */
public class ConcurrentCompositeHealthIndicator extends CompositeHealthIndicator {

  private final ThreadPoolTaskExecutor executor;

  private final HealthAggregator aggregator;

  public ConcurrentCompositeHealthIndicator(HealthAggregator healthAggregator,
                                            HealthIndicatorRegistry registry, ThreadPoolTaskExecutor executor) {
    super(healthAggregator, registry);
    this.executor = executor;
    this.aggregator = healthAggregator;
  }

  @Override
  public Health health() {
    Map<String, Future<Health>> futureHealths = getRegistry().getAll().entrySet()
        .stream()
        .map((entry) -> new SimpleEntry<>(entry.getKey(),
            this.executor.submit(() -> entry.getValue().health())))
        .collect(LinkedHashMap::new, (map, e) -> map.put(e.getKey(), e.getValue()), Map::putAll);

    Map<String, Health> healths = futureHealths.entrySet().stream()
        .map((entry) -> new SimpleEntry<>(entry.getKey(), get(entry.getValue())))
        .collect(LinkedHashMap::new, (map, e) -> map.put(e.getKey(), e.getValue()), Map::putAll);

    return this.aggregator.aggregate(healths);
  }

  private Health get(Future<Health> entry) {
    try {
      return entry.get(5000, TimeUnit.MILLISECONDS);
    }
    catch (TimeoutException | InterruptedException | ExecutionException ex) {
      return Health.down().withException(ex).build();
    }
  }

}
