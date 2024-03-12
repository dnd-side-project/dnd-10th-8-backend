package ac.dnd.mour.server.acceptance.schedule;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.common.AcceptanceTest;
import ac.dnd.mour.server.common.containers.callback.DatabaseCleanerEachCallbackExtension;
import ac.dnd.mour.server.common.fixture.ScheduleFixture;
import ac.dnd.mour.server.group.domain.model.GroupResponse;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ac.dnd.mour.server.acceptance.group.GroupAcceptanceStep.관리하고_있는_특정_그룹의_ID를_조회한다;
import static ac.dnd.mour.server.acceptance.heart.HeartAcceptanceStep.지출이_기록되지_않는_일정에_대한_마음을_생성한다;
import static ac.dnd.mour.server.acceptance.relation.RelationAcceptanceStep.관계를_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.알람_동기화를_위한_일정을_조회한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.일정_상세_정보를_조회한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_삭제한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_생성하고_ID를_추출한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.일정을_숨긴다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.지출이_기록되지_않은_일정을_조회한다;
import static ac.dnd.mour.server.acceptance.schedule.ScheduleAcceptanceStep.캘린더_Year_Month에_해당하는_일정을_조회한다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.결혼식;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.친구_XXX_생일;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.특별한_일정_XXX;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(DatabaseCleanerEachCallbackExtension.class)
@DisplayName("[Acceptance Test] 등록한 일정 관련 조회")
public class GetScheduleAcceptanceTest extends AcceptanceTest {
    private final LocalDate now = LocalDate.now();

    @Nested
    @DisplayName("일정 상세 조회 API")
    class GetScheduleDetails {
        @Test
        @DisplayName("일정 상세 정보를 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long scheduleId = 일정을_생성하고_ID를_추출한다(relationId, 결혼식, member.accessToken());

            일정_상세_정보를_조회한다(scheduleId, member.accessToken())
                    .statusCode(OK.value())
                    .body("id", is((int) scheduleId))
                    .body("relation.id", is((int) relationId))
                    .body("relation.name", is("관계-친구XXX-1"))
                    .body("relation.group.id", is((int) groupId))
                    .body("relation.group.name", is("친구"))
                    .body("day", is(결혼식.getDay().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                    .body("event", is(결혼식.getEvent()))
                    .body("repeatType", (결혼식.getRepeat() != null) ? is(결혼식.getRepeat().getType().getValue()) : nullValue())
                    .body("repeatFinish", (결혼식.getRepeat() != null && 결혼식.getRepeat().getFinish() != null) ? is(결혼식.getRepeat().getFinish().format(DateTimeFormatter.ISO_LOCAL_DATE)) : nullValue())
                    .body("alarm", (결혼식.getAlarm() != null) ? is(결혼식.getAlarm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) : nullValue())
                    .body("time", (결혼식.getTime() != null) ? is(결혼식.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME)) : nullValue())
                    .body("link", is(결혼식.getLink()))
                    .body("location", is(결혼식.getLocation()))
                    .body("memo", is(결혼식.getMemo()));
        }
    }

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
            final long relationId4 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-4", null, null, member.accessToken());

            final long scheduleId1 = 일정을_생성하고_ID를_추출한다(relationId1, LocalDate.of(now.getYear() + 1, 1, 1), "일정1", 특별한_일정_XXX, member.accessToken());
            final long scheduleId2 = 일정을_생성하고_ID를_추출한다(relationId2, LocalDate.of(now.getYear() + 1, 1, 15), "일정2", 특별한_일정_XXX, member.accessToken());
            final long scheduleId3 = 일정을_생성하고_ID를_추출한다(relationId3, LocalDate.of(now.getYear() + 1, 1, 18), "일정3", 특별한_일정_XXX, member.accessToken());
            final long scheduleId4 = 일정을_생성하고_ID를_추출한다(relationId4, LocalDate.of(now.getYear() + 1, 1, 22), "일정4", 특별한_일정_XXX, member.accessToken());

            final ValidatableResponse response1 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response1,
                    List.of(scheduleId1, scheduleId2, scheduleId3),
                    List.of(relationId1, relationId2, relationId3),
                    List.of("관계-친구XXX-1", "관계-친구XXX-2", "관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(
                            LocalDate.of(now.getYear() + 1, 1, 1),
                            LocalDate.of(now.getYear() + 1, 1, 15),
                            LocalDate.of(now.getYear() + 1, 1, 18)
                    ),
                    List.of("일정1", "일정2", "일정3")
            );

            지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId2, 100_000_000, List.of("특별한 일정", "이제 기록", "2"), member.accessToken());
            final ValidatableResponse response2 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response2,
                    List.of(scheduleId1, scheduleId3),
                    List.of(relationId1, relationId3),
                    List.of("관계-친구XXX-1", "관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(
                            LocalDate.of(now.getYear() + 1, 1, 1),
                            LocalDate.of(now.getYear() + 1, 1, 18)
                    ),
                    List.of("일정1", "일정3")
            );

            일정을_숨긴다(scheduleId1, member.accessToken());
            final ValidatableResponse response3 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(
                    response3,
                    List.of(scheduleId3),
                    List.of(relationId3),
                    List.of("관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구")),
                    List.of(LocalDate.of(now.getYear() + 1, 1, 18)),
                    List.of("일정3")
            );

            지출이_기록되지_않는_일정에_대한_마음을_생성한다(scheduleId3, 300_000_000, List.of("특별한 일정", "이제 기록", "1"), member.accessToken());
            final ValidatableResponse response4 = 지출이_기록되지_않은_일정을_조회한다(member.accessToken()).statusCode(OK.value());
            assertUnrecordedSchedulesMatch(response4, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
    }

    @Nested
    @DisplayName("캘린더에 등록한 일정 조회")
    class GetCalendarSchedule {
        @Test
        @DisplayName("특정 Year/Month 캘린더에 등록한 일정을 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId1 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long relationId2 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-2", null, null, member.accessToken());
            final long relationId3 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-3", null, null, member.accessToken());

            final long scheduleId1 = 일정을_생성하고_ID를_추출한다(relationId1, 결혼식, LocalDate.of(now.getYear() + 1, 1, 1), member.accessToken());
            final long scheduleId2 = 일정을_생성하고_ID를_추출한다(relationId2, 특별한_일정_XXX, LocalDate.of(now.getYear() + 1, 1, 15), member.accessToken());
            final long scheduleId3 = 일정을_생성하고_ID를_추출한다(relationId3, 친구_XXX_생일, LocalDate.of(now.getYear() + 1, 2, 1), member.accessToken());

            final ValidatableResponse response1 = 캘린더_Year_Month에_해당하는_일정을_조회한다(now.getYear() + 1, 1, member.accessToken());
            assertCalendarSchedulesMatch(
                    response1,
                    List.of(scheduleId1, scheduleId2),
                    List.of(relationId1, relationId2),
                    List.of("관계-친구XXX-1", "관계-친구XXX-2"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(결혼식, 특별한_일정_XXX),
                    List.of(LocalDate.of(now.getYear() + 1, 1, 1), LocalDate.of(now.getYear() + 1, 1, 15))
            );

            final ValidatableResponse response2 = 캘린더_Year_Month에_해당하는_일정을_조회한다(now.getYear() + 1, 2, member.accessToken());
            assertCalendarSchedulesMatch(
                    response2,
                    List.of(scheduleId3),
                    List.of(relationId3),
                    List.of("관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구")),
                    List.of(친구_XXX_생일),
                    List.of(LocalDate.of(now.getYear() + 1, 2, 1))
            );

            final ValidatableResponse response3 = 캘린더_Year_Month에_해당하는_일정을_조회한다(now.getYear() + 1, 3, member.accessToken());
            assertCalendarSchedulesMatch(response3, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
    }

    @Nested
    @DisplayName("알람 동기화를 위한 일정 조회 API")
    class GetSchedulesForAlarm {
        @Test
        @DisplayName("알람 동기화를 위한 일정을 조회한다")
        void success() {
            final AuthMember member = MEMBER_1.회원가입과_로그인을_진행한다();
            final long groupId = 관리하고_있는_특정_그룹의_ID를_조회한다("친구", member.accessToken());
            final long relationId1 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-1", null, null, member.accessToken());
            final long relationId2 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-2", null, null, member.accessToken());
            final long relationId3 = 관계를_생성하고_ID를_추출한다(groupId, "관계-친구XXX-3", null, null, member.accessToken());

            final long scheduleId1 = 일정을_생성하고_ID를_추출한다(relationId1, 결혼식, member.accessToken());
            final long scheduleId2 = 일정을_생성하고_ID를_추출한다(relationId2, 특별한_일정_XXX, member.accessToken()); // alarm null
            final long scheduleId3 = 일정을_생성하고_ID를_추출한다(relationId3, 친구_XXX_생일, member.accessToken());

            final ValidatableResponse response1 = 알람_동기화를_위한_일정을_조회한다(member.accessToken());
            assertSchedulesForAlarmMatch(
                    response1,
                    List.of(scheduleId1, scheduleId3),
                    List.of(relationId1, relationId3),
                    List.of("관계-친구XXX-1", "관계-친구XXX-3"),
                    List.of(new GroupResponse(groupId, "친구"), new GroupResponse(groupId, "친구")),
                    List.of(결혼식, 친구_XXX_생일)
            );

            일정을_삭제한다(scheduleId3, member.accessToken());
            final ValidatableResponse response2 = 알람_동기화를_위한_일정을_조회한다(member.accessToken());
            assertSchedulesForAlarmMatch(
                    response2,
                    List.of(scheduleId1),
                    List.of(relationId1),
                    List.of("관계-친구XXX-1"),
                    List.of(new GroupResponse(groupId, "친구")),
                    List.of(결혼식)
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

    private void assertCalendarSchedulesMatch(
            final ValidatableResponse response,
            final List<Long> ids,
            final List<Long> relationIds,
            final List<String> relationNames,
            final List<GroupResponse> groups,
            final List<ScheduleFixture> fixtures,
            final List<LocalDate> days
    ) {
        final int totalSize = ids.size();
        response.body("result", hasSize(totalSize));

        for (int i = 0; i < totalSize; i++) {
            final String index = String.format("result[%d]", i);
            final Long id = ids.get(i);
            final Long relationId = relationIds.get(i);
            final String relationName = relationNames.get(i);
            final GroupResponse group = groups.get(i);
            final ScheduleFixture fixture = fixtures.get(i);
            final LocalDate day = days.get(i);

            response
                    .body(index + ".id", is(id.intValue()))
                    .body(index + ".relation.id", is(relationId.intValue()))
                    .body(index + ".relation.name", is(relationName))
                    .body(index + ".relation.group.id", is((int) group.id()))
                    .body(index + ".relation.group.name", is(group.name()))
                    .body(index + ".day", is(day.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                    .body(index + ".event", is(fixture.getEvent()))
                    .body(index + ".time", is((fixture.getTime() != null) ? fixture.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME) : null))
                    .body(index + ".link", is(fixture.getLink()))
                    .body(index + ".location", is(fixture.getLocation()))
                    .body(index + ".memo", is(fixture.getMemo()));
        }
    }

    private void assertSchedulesForAlarmMatch(
            final ValidatableResponse response,
            final List<Long> ids,
            final List<Long> relationIds,
            final List<String> relationNames,
            final List<GroupResponse> groups,
            final List<ScheduleFixture> fixtures
    ) {
        final int totalSize = ids.size();
        response.body("result", hasSize(totalSize));

        for (int i = 0; i < totalSize; i++) {
            final String index = String.format("result[%d]", i);
            final Long id = ids.get(i);
            final Long relationId = relationIds.get(i);
            final String relationName = relationNames.get(i);
            final GroupResponse group = groups.get(i);
            final ScheduleFixture schedule = fixtures.get(i);

            response
                    .body(index + ".id", is(id.intValue()))
                    .body(index + ".relation.id", is(relationId.intValue()))
                    .body(index + ".relation.name", is(relationName))
                    .body(index + ".relation.group.id", is((int) group.id()))
                    .body(index + ".relation.group.name", is(group.name()))
                    .body(index + ".day", is(schedule.getDay().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                    .body(index + ".event", is(schedule.getEvent()))
                    .body(index + ".repeatType", (schedule.getRepeat() != null) ? is(schedule.getRepeat().getType().getValue()) : nullValue())
                    .body(index + ".repeatFinish", (schedule.getRepeat() != null && schedule.getRepeat().getFinish() != null) ? is(schedule.getRepeat().getFinish().format(DateTimeFormatter.ISO_LOCAL_DATE)) : nullValue())
                    .body(index + ".alarm", (schedule.getAlarm() != null) ? is(schedule.getAlarm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) : nullValue())
                    .body(index + ".time", (schedule.getTime() != null) ? is(schedule.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME)) : nullValue())
                    .body(index + ".link", is(schedule.getLink()))
                    .body(index + ".location", is(schedule.getLocation()))
                    .body(index + ".memo", is(schedule.getMemo()));
        }
    }
}
