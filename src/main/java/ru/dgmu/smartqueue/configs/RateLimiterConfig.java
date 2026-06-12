package ru.dgmu.smartqueue.configs;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RateLimiterConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Bean
  public RedisClient redisClient() {
    return RedisClient.create(String.format("redis://%s:%d", redisHost, redisPort));
  }

  @Bean
  public LettuceBasedProxyManager<byte[]> proxyManager(RedisClient redisClient) {
    StatefulRedisConnection<byte[], byte[]> connection = redisClient.connect(
        RedisCodec.of(new ByteArrayCodec(), new ByteArrayCodec())
    );

    return LettuceBasedProxyManager.builderFor(connection)
        .withExpirationStrategy(ExpirationAfterWriteStrategy.fixedTimeToLive(Duration.ofMinutes(120)))
        .build();
  }
}
