package ac.dnd.mur.server.acceptance.member;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.내_정보를_수정한다;
import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.닉네임_중복_체크를_진행한다;
import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.서비스_탈퇴를_진행한다;
import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.회원가입을_진행한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_2;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 사용자 계정 관리 관련 기능")
public class ManageAccountAcceptanceTest extends AcceptanceTest {
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

    @Nested
    @DisplayName("내 정보 수정 API")
    class Update {
        @Test
        @DisplayName("내 정보 수정을 진행한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            내_정보를_수정한다(MEMBER_2, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("탈퇴 API")
    class Delete {
        @Test
        @DisplayName("서비스 탈퇴를 진행한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            닉네임_중복_체크를_진행한다(MEMBER_1.getNickname().getValue())
                    .statusCode(OK.value())
                    .body("result", is(false));

            서비스_탈퇴를_진행한다(member.accessToken())
                    .statusCode(NO_CONTENT.value());

            닉네임_중복_체크를_진행한다(MEMBER_1.getNickname().getValue())
                    .statusCode(OK.value())
                    .body("result", is(true));
        }
    }
}
