package ac.dnd.mour.server.global.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StringRedisOperator implements RedisOperator<String, String> {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(final String key, final String value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public void save(final String key, final String value, final Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean contains(final String key) {
        return redisTemplate.opsForValue().get(key) != null;
    }

    @Override
    public void delete(final String key) {
        redisTemplate.delete(key);
    }
}
