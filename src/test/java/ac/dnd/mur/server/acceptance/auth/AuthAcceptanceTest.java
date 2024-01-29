package ac.dnd.mur.server.acceptance.auth;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.auth.AuthAcceptanceStep.로그아웃을_진행한다;
import static ac.dnd.mur.server.acceptance.auth.AuthAcceptanceStep.로그인을_진행한다;
import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.회원가입을_진행한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 로그인/로그아웃")
public class AuthAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("로그인 API")
    class Login {
        @Test
        @DisplayName("DB에 SocialID에 대한 사용자 정보가 존재하지 않으면 404 응답과 함께 회원가입 플로우를 진행한다")
        void throwExceptionByNotFound() {
            로그인을_진행한다(MEMBER_1.getPlatform().getSocialId(), MEMBER_1.getPlatform().getEmail().getValue())
                    .statusCode(NOT_FOUND.value());
        }

        @Test
        @DisplayName("DB에 SocialID에 대한 사용자 정보가 존재하면 로그인 처리를 진행한다")
        void success() {
            회원가입을_진행한다(MEMBER_1);
            로그인을_진행한다(MEMBER_1.getPlatform().getSocialId(), MEMBER_1.getPlatform().getEmail().getValue())
                    .body("id", notNullValue(Long.class))
                    .body("accessToken", notNullValue(String.class))
                    .body("refreshToken", notNullValue(String.class));
        }
    }

    @Nested
    @DisplayName("로그아웃 API")
    class Logout {
        @Test
        @DisplayName("로그아웃을 진행한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            로그아웃을_진행한다(member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
