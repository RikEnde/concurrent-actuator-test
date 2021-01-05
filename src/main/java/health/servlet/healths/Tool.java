package health.servlet.healths;

import java.time.LocalDateTime;
import java.util.Random;

public class Tool {
    public static long SLEEPY_TIME = 1000;

    public static long sleep(long n, Object caller) {
        System.out.printf("%s - Start sleeping for %s on behalf of %s\n", LocalDateTime.now(), n, caller.getClass().getName());
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("%s - Done sleeping for %s on behalf of %s\n", LocalDateTime.now(), n, caller.getClass().getName());

        return n;
    }

    public static long sleep(Object caller) {
        long n = new Random().nextInt((int)SLEEPY_TIME);

        return sleep(new Random().nextInt((int)SLEEPY_TIME));
    }
}
