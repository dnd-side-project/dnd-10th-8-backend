package ac.dnd.mur.server.acceptance.auth;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.auth.AuthAcceptanceStep.토큰을_재발급받는다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 토큰 재발급")
public class TokenReissueAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("토큰 재발급 API")
    class TokenReissueApi {
        @Test
        @DisplayName("유효한 RefreshToken을 통해서 AccessToken + RefreshToken을 재발급받는다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            토큰을_재발급받는다(member.refreshToken())
                    .statusCode(OK.value())
                    .body("accessToken", notNullValue(String.class))
                    .body("refreshToken", notNullValue(String.class));
        }
    }
}
