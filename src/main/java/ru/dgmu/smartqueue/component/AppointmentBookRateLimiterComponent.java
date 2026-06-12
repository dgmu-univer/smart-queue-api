package ru.dgmu.smartqueue.component;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppointmentBookRateLimiterComponent {

    @Value("${app.appointments.book.attempts-count:2}")
    private Integer attemptsCount;

    @Value("${app.appointments.book.attempts-refresh-interval-in-min:60}")
    private Integer attemptsRefreshIntervalInMin;

    private final LettuceBasedProxyManager<byte[]> proxyManager;

    public AppointmentBookRateLimiterComponent(LettuceBasedProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String ip) {
        byte[] redisKey = ("cache:book_attempts:" + ip).getBytes();

        return proxyManager.builder().build(redisKey, () ->
            BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(attemptsCount)
                        .refillGreedy(attemptsCount, Duration.ofMinutes(attemptsRefreshIntervalInMin))
                        .build())
                .build()
        );
    }
}
