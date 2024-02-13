package ac.dnd.mour.server.acceptance.heart;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mour.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_삭제한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_생성한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.마음을_수정한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.지출이_기록되지_않는_일정에_대한_마음을_생성한다;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_받았다;
import static ac.dnd.mour.server.common.fixture.HeartFixture.승진_선물을_보냈다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.특별한_일정_XXX;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 마음 도메인 생명주기 관리 (생성, 수정, 삭제)")
public class ManageHeartAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("마음 생성 API")
    class Create {
        @Test
        @DisplayName("마음을 생성한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());

            마음을_생성한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            ).statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("지출이 기록되지 않는 일정에 대한 마음 생성 API")
    class ApplyUnrecordedHeat {
        @Test
        @DisplayName("지출이 기록되지 않는 일정에 대한 마음을 생성한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long scheduleId = 일정을_생성하고_ID를_추출한다(relationId, LocalDate.of(2024, 1, 1), "일정1", 특별한_일정_XXX, member.accessToken());

            지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId, 100_000_000, List.of("일정", "지금 기록"), member.accessToken())
                    .statusCode(OK.value())
                    .body("result", notNullValue(Long.class));
        }
    }

    @Nested
    @DisplayName("마음 수정 API")
    class Update {
        @Test
        @DisplayName("마음을 수정한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());
            final long heartId = 마음을_생성하고_ID를_추출한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );

            마음을_수정한다(
                    heartId,
                    승진_선물을_보냈다.getMoney(),
                    승진_선물을_보냈다.getDay(),
                    승진_선물을_보냈다.getEvent(),
                    승진_선물을_보냈다.getMemo(),
                    승진_선물을_보냈다.getTags(),
                    member.accessToken()
            ).statusCode(NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("마음 삭제 API")
    class Delete {
        @Test
        @DisplayName("마음을 삭제한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, 친구_1.getName(), 친구_1.getImageUrl(), 친구_1.getMemo(), member.accessToken());
            final long heartId = 마음을_생성하고_ID를_추출한다(
                    relationId,
                    결혼_축의금을_받았다.isGive(),
                    결혼_축의금을_받았다.getMoney(),
                    결혼_축의금을_받았다.getDay(),
                    결혼_축의금을_받았다.getEvent(),
                    결혼_축의금을_받았다.getMemo(),
                    결혼_축의금을_받았다.getTags(),
                    member.accessToken()
            );

            마음을_삭제한다(heartId, member.accessToken())
                    .statusCode(NO_CONTENT.value());
        }
    }
}
