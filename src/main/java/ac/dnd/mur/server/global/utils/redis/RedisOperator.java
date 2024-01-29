package ac.dnd.mur.server.global.utils.redis;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface RedisOperator<K, V> {
    void save(final K key, final V value, final long timeout, final TimeUnit timeUnit);

    void save(final K key, final V value, final Duration duration);

    V get(final K key);

    boolean contains(final K key);

    void delete(final K key);
}
