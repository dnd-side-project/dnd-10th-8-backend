package ac.dnd.mur.server.auth.infrastructure.token;

import ac.dnd.mur.server.auth.application.adapter.TokenStore;
import ac.dnd.mur.server.global.utils.redis.RedisOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
public class RedisTokenStore implements TokenStore {
    private final long refreshTokenValidityInSeconds;
    private final RedisOperator<String, String> redisOperator;

    public RedisTokenStore(
            @Value("${jwt.refresh-token-validity-seconds}") final long refreshTokenValidityInSeconds,
            final RedisOperator<String, String> redisOperator
    ) {
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
        this.redisOperator = redisOperator;
    }

    @Override
    public void synchronizeRefreshToken(final Long memberId, final String refreshToken) {
        redisOperator.save(createKey(memberId), refreshToken, Duration.ofSeconds(refreshTokenValidityInSeconds));
    }

    @Override
    public void updateRefreshToken(final long memberId, final String newRefreshToken) {
        redisOperator.save(createKey(memberId), newRefreshToken, Duration.ofSeconds(refreshTokenValidityInSeconds));
    }

    @Override
    public void deleteRefreshToken(final long memberId) {
        redisOperator.delete(createKey(memberId));
    }

    @Override
    public boolean isMemberRefreshToken(final long memberId, final String refreshToken) {
        final String validToken = redisOperator.get(createKey(memberId));
        return Objects.equals(refreshToken, validToken);
    }

    private String createKey(final long memberId) {
        return String.format("TOKEN:%s", memberId);
    }
}
