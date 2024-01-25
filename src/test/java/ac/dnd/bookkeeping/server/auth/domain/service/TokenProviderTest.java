package ac.dnd.bookkeeping.server.auth.domain.service;

import ac.dnd.bookkeeping.server.auth.exception.AuthException;
import ac.dnd.bookkeeping.server.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.auth.exception.AuthExceptionCode.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Auth -> TokenProvider 테스트")
class TokenProviderTest extends UnitTest {
    private static final long MEMBER_ID = 1L;
    private static final String SECRET_KEY = "0aad1b3d890f6f4fb89555708462aa50b1a4f5808fe453602beec25ecadf297f";

    private final TokenProvider invalidProvider = new TokenProvider(SECRET_KEY, 0L, 0L);
    private final TokenProvider validProvider = new TokenProvider(SECRET_KEY, 7200L, 7200L);

    @Test
    @DisplayName("Token의 Payload를 추출한다")
    void extractPayload() {
        // when
        final String accessToken = validProvider.createAccessToken(MEMBER_ID);
        final String refreshToken = validProvider.createRefreshToken(MEMBER_ID);

        // then
        assertAll(
                () -> assertThat(validProvider.getId(accessToken)).isEqualTo(MEMBER_ID),
                () -> assertThat(validProvider.getId(refreshToken)).isEqualTo(MEMBER_ID)
        );
    }

    @Test
    @DisplayName("Token 만료에 대한 유효성을 검증한다")
    void expiredToken() {
        // when
        final String validToken = validProvider.createAccessToken(MEMBER_ID);
        final String invalidToken = invalidProvider.createAccessToken(MEMBER_ID);

        // then
        assertDoesNotThrow(() -> validProvider.validateToken(validToken));
        assertThatThrownBy(() -> invalidProvider.validateToken(invalidToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("Token 조작에 대한 유효성을 검증한다")
    void malformedToken() {
        // when
        final String forgedToken = validProvider.createAccessToken(MEMBER_ID) + "hacked";

        // then
        assertThatThrownBy(() -> validProvider.validateToken(forgedToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }
}
