package ac.dnd.bookkeeping.server.auth.infrastructure.token;

import ac.dnd.bookkeeping.server.common.RedisTest;
import ac.dnd.bookkeeping.server.global.utils.redis.RedisOperator;
import ac.dnd.bookkeeping.server.global.utils.redis.StringRedisOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Duration;

import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import({RedisTokenStore.class, StringRedisOperator.class})
@DisplayName("Auth -> RedisTokenStore 테스트")
class RedisTokenStoreTest extends RedisTest {
    @Autowired
    private RedisTokenStore sut;

    @Autowired
    private RedisOperator<String, String> redisOperator;

    private static final String KEY = "TOKEN:%s";
    private static final Long MEMBER_ID = 1L;

    @Nested
    @DisplayName("RefreshToken 동기화")
    class SynchronizeRefreshToken {
        @Test
        @DisplayName("RefreshToken을 보유하고 있지 않은 사용자에게는 새로운 RefreshToken을 발급한다")
        void reissue() {
            // when
            sut.synchronizeRefreshToken(MEMBER_ID, REFRESH_TOKEN);

            // then
            final String token = redisOperator.get(createKey());
            assertAll(
                    () -> assertThat(token).isNotNull(),
                    () -> assertThat(token).isEqualTo(REFRESH_TOKEN)
            );
        }

        @Test
        @DisplayName("RefreshToken을 보유하고 있는 사용자에게는 새로운 RefreshToken으로 업데이트한다")
        void update() {
            // given
            redisOperator.save(createKey(), REFRESH_TOKEN, Duration.ofSeconds(1234));

            // when
            final String newRefreshToken = REFRESH_TOKEN + "new";
            sut.synchronizeRefreshToken(MEMBER_ID, newRefreshToken);

            // then
            final String token = redisOperator.get(createKey());
            assertAll(
                    () -> assertThat(token).isNotNull(),
                    () -> assertThat(token).isNotEqualTo(REFRESH_TOKEN),
                    () -> assertThat(token).isEqualTo(newRefreshToken)
            );
        }
    }

    @Test
    @DisplayName("사용자가 보유하고 있는 RefreshToken을 재발급한다")
    void updateRefreshToken() {
        // given
        redisOperator.save(createKey(), REFRESH_TOKEN, Duration.ofSeconds(1234));

        // when
        final String newRefreshToken = REFRESH_TOKEN + "new";
        sut.updateRefreshToken(MEMBER_ID, newRefreshToken);

        // then
        final String token = redisOperator.get(createKey());
        assertAll(
                () -> assertThat(token).isNotNull(),
                () -> assertThat(token).isNotEqualTo(REFRESH_TOKEN),
                () -> assertThat(token).isEqualTo(newRefreshToken)
        );
    }

    @Test
    @DisplayName("사용자가 보유하고 있는 RefreshToken을 삭제한다")
    void deleteRefreshToken() {
        // given
        redisOperator.save(createKey(), REFRESH_TOKEN, Duration.ofSeconds(1234));

        // when
        sut.deleteRefreshToken(MEMBER_ID);

        // then
        final String token = redisOperator.get(createKey());
        assertThat(token).isNull();
    }

    @Test
    @DisplayName("사용자 소유의 RefreshToken인지 확인한다")
    void isMemberRefreshToken() {
        // given
        redisOperator.save(createKey(), REFRESH_TOKEN, Duration.ofSeconds(1234));

        // when
        final boolean actual1 = sut.isMemberRefreshToken(MEMBER_ID, REFRESH_TOKEN);
        final boolean actual2 = sut.isMemberRefreshToken(MEMBER_ID, REFRESH_TOKEN + "fake");

        // then
        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse()
        );
    }

    private String createKey() {
        return String.format(KEY, MEMBER_ID);
    }
}
