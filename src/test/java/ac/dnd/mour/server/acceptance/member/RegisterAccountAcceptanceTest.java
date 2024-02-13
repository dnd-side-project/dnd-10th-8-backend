package ac.dnd.mour.server.acceptance.member;

import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mour.server.acceptance.member.MemberAcceptanceStep.닉네임_중복_체크를_진행한다;
import static ac.dnd.mour.server.acceptance.member.MemberAcceptanceStep.회원가입을_진행한다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 회원가입 플로우 관련 기능")
public class RegisterAccountAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("닉네임 중복 체크 API")
    class CheckNickname {
        @Test
        @DisplayName("사용할 수 있는 닉네임인지 확인한다 [True]")
        void resultTrue() {
            닉네임_중복_체크를_진행한다("test")
                    .statusCode(OK.value())
                    .body("result", is(true));
        }

        @Test
        @DisplayName("사용할 수 있는 닉네임인지 확인한다 [False]")
        void resultFalse() {
            MEMBER_1.회원가입과_로그인을_진행한다();
            닉네임_중복_체크를_진행한다(MEMBER_1.getNickname().getValue())
                    .statusCode(OK.value())
                    .body("result", is(false));
        }
    }

    @Nested
    @DisplayName("회원가입 API")
    class Register {
        @Test
        @DisplayName("회원가입을 진행한다")
        void success() {
            회원가입을_진행한다(MEMBER_1)
                    .statusCode(OK.value())
                    .body("id", notNullValue(Long.class))
                    .body("accessToken", notNullValue(String.class))
                    .body("refreshToken", notNullValue(String.class));
        }
    }
}
