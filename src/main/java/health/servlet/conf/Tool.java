package health.servlet.conf;

import java.time.LocalDateTime;
import java.util.Random;

public class Tool {
  public static long sleep(long n, Object caller) {
    System.err.printf("%s - Start sleeping for %s on behalf of %s\n", LocalDateTime.now(), n, caller.getClass().getName());
    try {
      Thread.sleep(n);
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
    System.err.printf("%s - Done sleeping for %s on behalf of %s\n", LocalDateTime.now(), n, caller.getClass().getName());

    return n;
  }

  public static long sleep(Object caller) {
    long n = new Random().nextInt(1000);

    return sleep(new Random().nextInt(1000));
  }
}
