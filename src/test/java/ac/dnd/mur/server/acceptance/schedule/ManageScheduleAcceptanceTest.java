package ac.dnd.mur.server.acceptance.schedule;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import ac.dnd.mur.server.common.fixture.ScheduleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_삭제한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_생성한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_수정한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 일정 도메인 생명주기 관리 (생성, 수정, 삭제)")
public class ManageScheduleAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("일정 생성 API")
    class Create {
        @Test
        @DisplayName("일정을 생성한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            일정을_생성한다(relationId, ScheduleFixture.결혼식, member.accessToken())
                    .statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("일정 수정 API")
    class Update {
        @Test
        @DisplayName("일정을 수정한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            final long scheduleId = 일정을_생성하고_ID를_추출한다(relationId, ScheduleFixture.결혼식, member.accessToken());
            일정을_수정한다(scheduleId, ScheduleFixture.친구_XXX_생일, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("일정 삭제 API")
    class Delete {
        @Test
        @DisplayName("일정을 삭제한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            final long scheduleId = 일정을_생성하고_ID를_추출한다(relationId, ScheduleFixture.결혼식, member.accessToken());
            일정을_삭제한다(scheduleId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
