package ac.dnd.mur.server.acceptance.schedule;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.AcceptanceTest;
import ac.dnd.mur.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ac.dnd.mur.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mur.server.acceptance.heart.HeartAcceptanceStep.지출이_기록되지_않는_일정에_대한_마음을_생성한다;
import static ac.dnd.mur.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_생성하고_ID를_추출한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.지출이_기록되지_않은_일정을_조회한다;
import static ac.dnd.mur.server.acceptance.schedule.ScheduleAcceptanceStep.캘린더_Year_Month에_해당하는_일정을_조회한다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.ScheduleFixture.특별한_일정_XXX;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 등록한 일정 관련 조회")
public class GetScheduleAcceptanceTest extends AcceptanceTest {
    @Nested
    @DisplayName("지출(보낸 마음)이 기록되지 않은 일정 조회 API")
    class GetUnrecordedSchedule {
        @Test
        @DisplayName("지출(보낸 마음)이 기록되지 않은 일정을 조회한다 - UnrecordedStandardDefiner = 2024/01/20")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId1 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long relationId2 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-2", null, null, member.accessToken());
            final long relationId3 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-3", null, null, member.accessToken());
            final long scheduleId1 = 일정을_생성하고_ID를_추출한다(
                    relationId1,
                    LocalDate.of(2024, 1, 1),
                    "일정1",
                    특별한_일정_XXX,
                    member.accessToken()
            );
            final long scheduleId2 = 일정을_생성하고_ID를_추출한다(
                    relationId2,
                    LocalDate.of(2024, 1, 15),
                    "일정2",
                    특별한_일정_XXX,
                    member.accessToken()
            );
            final long scheduleId3 = 일정을_생성하고_ID를_추출한다(
                    relationId3,
                    LocalDate.of(2024, 1, 22),
                    "일정3",
                    특별한_일정_XXX,
                    member.accessToken()
            );

            final ValidatableResponse response1 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response1,
                    List.of(scheduleId1, scheduleId2),
                    List.of(relationId1, relationId2),
                    List.of("관계-친구XXX-1", "관계-친구XXX-2"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15)),
                    List.of("일정1", "일정2")
            );

            지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId2, 100_000_000, List.of("특별한 일정", "이제 기록", "2"), member.accessToken());
            final ValidatableResponse response2 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response2,
                    List.of(scheduleId1),
                    List.of(relationId1),
                    List.of("관계-친구XXX-1"),
                    List.of(new GroupResponse(groupId, "친구")),
                    List.of(LocalDate.of(2024, 1, 1)),
                    List.of("일정1")
            );

            지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId1, 300_000_000, List.of("특별한 일정", "이제 기록", "1"), member.accessToken());
            final ValidatableResponse response3 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response3,
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of()
            );
        }
    }

    @Nested
    @DisplayName("지출(보낸 마음)이 기록되지 않은 일정 조회 API")
    class GetCalendarSchedule {
        @Test
        @DisplayName("지출(보낸 마음)이 기록되지 않은 일정을 조회한다 - UnrecordedStandardDefiner = 2024/01/20")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId1 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long relationId2 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-2", null, null, member.accessToken());
            final long relationId3 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-3", null, null, member.accessToken());
            final long scheduleId1 = 일정을_생성하고_ID를_추출한다(
                    relationId1,
                    LocalDate.of(2024, 1, 1),
                    "일정1",
                    특별한_일정_XXX,
                    member.accessToken()
            );
            final long scheduleId2 = 일정을_생성하고_ID를_추출한다(
                    relationId2,
                    LocalDate.of(2024, 1, 15),
                    "일정2",
                    특별한_일정_XXX,
                    member.accessToken()
            );
            final long scheduleId3 = 일정을_생성하고_ID를_추출한다(
                    relationId3,
                    LocalDate.of(2024, 2, 1),
                    "일정3",
                    특별한_일정_XXX,
                    member.accessToken()
            );

            final ValidatableResponse response1 = 캘린더_Year_Month에_해당하는_일정을_조회한다(2024, 1, member.accessToken());
            assertUnrecordedSchedulesMatch(
                    response1,
                    List.of(scheduleId1, scheduleId2),
                    List.of(relationId1, relationId2),
                    List.of("관계-친구XXX-1", "관계-친구XXX-2"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15)),
                    List.of("일정1", "일정2")
            );

            final ValidatableResponse response2 = 캘린더_Year_Month에_해당하는_일정을_조회한다(2024, 2, member.accessToken());
            assertUnrecordedSchedulesMatch(
                    response2,
                    List.of(scheduleId3),
                    List.of(relationId3),
                    List.of("관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구")),
                    List.of(LocalDate.of(2024, 2, 1)),
                    List.of("일정3")
            );

            final ValidatableResponse response3 = 캘린더_Year_Month에_해당하는_일정을_조회한다(2024, 3, member.accessToken());
            assertUnrecordedSchedulesMatch(
                    response3,
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of()
            );
        }
    }

    private void assertUnrecordedSchedulesMatch(
            final ValidatableResponse response,
            final List<Long> ids,
            final List<Long> relationIds,
            final List<String> relationNames,
            final List<GroupResponse> groups,
            final List<LocalDate> days,
            final List<String> events
    ) {
        final int totalSize = ids.size();
        response.body("result", hasSize(totalSize));

        for (int i = 0; i < totalSize; i++) {
            final String index = String.format("result[%d]", i);
            final Long id = ids.get(i);
            final Long relationId = relationIds.get(i);
            final String relationName = relationNames.get(i);
            final GroupResponse group = groups.get(i);
            final LocalDate day = days.get(i);
            final String event = events.get(i);

            response
                    .body(index + ".id", is(id.intValue()))
                    .body(index + ".relation.id", is(relationId.intValue()))
                    .body(index + ".relation.name", is(relationName))
                    .body(index + ".relation.group.id", is((int) group.id()))
                    .body(index + ".relation.group.name", is(group.name()))
                    .body(index + ".day", is(day.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                    .body(index + ".event", is(event))
                    .body(index + ".time", nullValue())
                    .body(index + ".link", nullValue())
                    .body(index + ".location", nullValue());
        }
    }
}
