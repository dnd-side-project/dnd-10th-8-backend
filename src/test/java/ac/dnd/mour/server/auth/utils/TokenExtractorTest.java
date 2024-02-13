package ac.dnd.mour.server.auth.utils;

import ac.dnd.mour.server.common.UnitTest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ac.dnd.mour.server.auth.domain.model.AuthToken.ACCESS_TOKEN_HEADER;
import static ac.dnd.mour.server.auth.domain.model.AuthToken.REFRESH_TOKEN_HEADER;
import static ac.dnd.mour.server.auth.domain.model.AuthToken.TOKEN_TYPE;
import static ac.dnd.mour.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.mour.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Auth -> TokenExtractor 테스트")
class TokenExtractorTest extends UnitTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Nested
    @DisplayName("AccessToken 추출")
    class ExtractAccessToken {
        @Test
        @DisplayName("HTTP Request Message의 [Authorization Header]에 토큰이 없다면 Optional 빈 값을 응답한다")
        void emptyToken() {
            // given
            given(request.getHeader(ACCESS_TOKEN_HEADER)).willReturn(null);

            // when
            final Optional<String> token = TokenExtractor.extractAccessToken(request);

            // then
            assertThat(token).isEmpty();
        }

        @Test
        @DisplayName("HTTP Request Message의 [Authorization Header]에 토큰 타입만 명시되었다면 Optional 빈 값을 응답한다")
        void emptyTokenWithType() {
            // given
            given(request.getHeader(ACCESS_TOKEN_HEADER)).willReturn(TOKEN_TYPE);

            // when
            final Optional<String> token = TokenExtractor.extractAccessToken(request);

            // then
            assertThat(token).isEmpty();
        }

        @Test
        @DisplayName("HTTP Request Message의 [Authorization Header]에 토큰이 있다면 Optional로 감싸서 응답한다")
        void success() {
            // given
            final String tokenHeader = String.join(" ", TOKEN_TYPE, ACCESS_TOKEN);
            given(request.getHeader(ACCESS_TOKEN_HEADER)).willReturn(tokenHeader);

            // when
            final Optional<String> token = TokenExtractor.extractAccessToken(request);

            // then
            assertAll(
                    () -> assertThat(token).isPresent(),
                    () -> assertThat(token.get()).isEqualTo(ACCESS_TOKEN)
            );
        }
    }

    @Nested
    @DisplayName("RefreshToken 추출")
    class ExtractRefreshToken {
        @Test
        @DisplayName("HTTP Request Message의 [Token-Refresh Header]에 토큰이 없다면 Optional 빈 값을 응답한다")
        void emptyToken() {
            // given
            given(request.getHeader(REFRESH_TOKEN_HEADER)).willReturn(null);

            // when
            final Optional<String> token = TokenExtractor.extractRefreshToken(request);

            // then
            assertThat(token).isEmpty();
        }

        @Test
        @DisplayName("HTTP Request Message의 [Token-Refresh Header]에 토큰 타입만 명시되었다면 Optional 빈 값을 응답한다")
        void emptyTokenWithType() {
            // given
            given(request.getHeader(REFRESH_TOKEN_HEADER)).willReturn(TOKEN_TYPE);

            // when
            final Optional<String> token = TokenExtractor.extractRefreshToken(request);

            // then
            assertThat(token).isEmpty();
        }

        @Test
        @DisplayName("HTTP Request Message의 [Token-Refresh Header]에 토큰이 있다면 Optional로 감싸서 응답한다")
        void success() {
            // given
            final String tokenHeader = String.join(" ", TOKEN_TYPE, REFRESH_TOKEN);
            given(request.getHeader(REFRESH_TOKEN_HEADER)).willReturn(tokenHeader);

            // when
            final Optional<String> token = TokenExtractor.extractRefreshToken(request);

            // then
            assertAll(
                    () -> assertThat(token).isPresent(),
                    () -> assertThat(token.get()).isEqualTo(REFRESH_TOKEN)
            );
        }
    }
}
