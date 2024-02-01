package ac.dnd.mur.server.acceptance.member;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.member.MemberAcceptanceStep.마이페이지_내_정보를_조회한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 사용자 프로필 조회 관련 기능")
public class MemberProfileQueryAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("마이페이지 내 정보 조회 API")
    class GetPrivateProfile {
        @Test
        @DisplayName("마이페이지 내 정보 조회를 진행한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            마이페이지_내_정보를_조회한다(member.accessToken())
                    .statusCode(OK.value())
                    .body("id", notNullValue(Long.class))
                    .body("email", is(MEMBER_1.getPlatform().getEmail().getValue()))
                    .body("profileImageUrl", is(MEMBER_1.getProfileImageUrl()))
                    .body("name", is(MEMBER_1.getName()))
                    .body("nickname", is(MEMBER_1.getNickname().getValue()))
                    .body("gender", is(MEMBER_1.getGender().getValue()))
                    .body("birth", is(MEMBER_1.getBirth().toString()));
        }
    }
}
