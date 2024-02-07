package ac.dnd.mur.server.schedule.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.response.RelationSummary;
import ac.dnd.mur.server.schedule.application.usecase.GetCalendarScheduleUseCase;
import ac.dnd.mur.server.schedule.application.usecase.GetSchedulesForAlarmUseCase;
import ac.dnd.mur.server.schedule.application.usecase.GetUnrecordedScheduleUseCase;
import ac.dnd.mur.server.schedule.application.usecase.query.response.CalendarScheduleResponse;
import ac.dnd.mur.server.schedule.application.usecase.query.response.SchedulesForAlarmResponse;
import ac.dnd.mur.server.schedule.application.usecase.query.response.UnrecordedScheduleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ac.dnd.mur.server.common.fixture.GroupFixture.친구;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.fixture.ScheduleFixture.친구_XXX_생일;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Schedule -> GetScheduleApiController 테스트")
class GetScheduleApiControllerTest extends ControllerTest {
    @Autowired
    private GetUnrecordedScheduleUseCase getUnrecordedScheduleUseCase;

    @Autowired
    private GetCalendarScheduleUseCase getCalendarScheduleUseCase;

    @Autowired
    private GetSchedulesForAlarmUseCase getSchedulesForAlarmUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("지출(보낸 마음)이 기록되지 않은 일정 조회 API [GET /api/v1/schedules/unrecorded]")
    class GetUnrecordedSchedule {
        private static final String BASE_URL = "/api/v1/schedules/unrecorded";

        @Test
        @DisplayName("지출(보낸 마음)이 기록되지 않은 일정을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getUnrecordedScheduleUseCase.invoke(member.getId())).willReturn(List.of(
                    new UnrecordedScheduleResponse(
                            10L,
                            new RelationSummary(
                                    1L,
                                    친구_1.getName(),
                                    new GroupResponse(1L, 친구.getName())
                            ),
                            LocalDate.of(2024, 1, 31),
                            친구_XXX_생일.getEvent(),
                            친구_XXX_생일.getTime(),
                            친구_XXX_생일.getLink(),
                            친구_XXX_생일.getLocation()
                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL),
                    status().isOk(),
                    successDocsWithAccessToken("ScheduleApi/UnrecordedHeart", createHttpSpecSnippets(
                            responseFields(
                                    body("result[].id", "일정 ID(PK)"),
                                    body("result[].relation.id", "관계 ID(PK)"),
                                    body("result[].relation.name", "관계 이름"),
                                    body("result[].relation.group.id", "그룹 ID(PK)"),
                                    body("result[].relation.group.name", "그룹명"),
                                    body("result[].day", "날짜"),
                                    body("result[].event", "행사 종류"),
                                    body("result[].time", "시간", "Nullable"),
                                    body("result[].link", "링크", "Nullable"),
                                    body("result[].location", "위치", "Nullable")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("캘린더 Year/Month 일정 조회 API [GET /api/v1/schedules/me]")
    class GetCalendarSchedule {
        private static final String BASE_URL = "/api/v1/schedules/me";

        @Test
        @DisplayName("지출(보낸 마음)이 기록되지 않은 일정을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getCalendarScheduleUseCase.invoke(any())).willReturn(List.of(
                    new CalendarScheduleResponse(
                            10L,
                            new RelationSummary(
                                    1L,
                                    친구_1.getName(),
                                    new GroupResponse(1L, 친구.getName())
                            ),
                            LocalDate.of(2024, 1, 31),
                            친구_XXX_생일.getEvent(),
                            친구_XXX_생일.getTime(),
                            친구_XXX_생일.getLink(),
                            친구_XXX_생일.getLocation()
                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL, Map.of(
                            "year", "2024",
                            "month", "1"
                    )),
                    status().isOk(),
                    successDocsWithAccessToken("ScheduleApi/Calendar", createHttpSpecSnippets(
                            queryParameters(
                                    query("year", "Year", true),
                                    query("month", "Month", true)
                            ),
                            responseFields(
                                    body("result[].id", "일정 ID(PK)"),
                                    body("result[].relation.id", "관계 ID(PK)"),
                                    body("result[].relation.name", "관계 이름"),
                                    body("result[].relation.group.id", "그룹 ID(PK)"),
                                    body("result[].relation.group.name", "그룹명"),
                                    body("result[].day", "날짜"),
                                    body("result[].event", "행사 종류"),
                                    body("result[].time", "시간", "Nullable"),
                                    body("result[].link", "링크", "Nullable"),
                                    body("result[].location", "위치", "Nullable")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("알람 동기화를 위한 일정 조회 API [GET /api/v1/schedules/me/alarm]")
    class GetSchedulesForAlarm {
        private static final String BASE_URL = "/api/v1/schedules/me/alarm";

        @Test
        @DisplayName("알람 동기화를 위한 일정을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getSchedulesForAlarmUseCase.invoke(member.getId())).willReturn(List.of(
                    new SchedulesForAlarmResponse(
                            10L,
                            new RelationSummary(
                                    1L,
                                    친구_1.getName(),
                                    new GroupResponse(1L, 친구.getName())
                            ),
                            LocalDate.of(2024, 1, 31),
                            친구_XXX_생일.getEvent(),
                            친구_XXX_생일.getRepeat().getType().getValue(),
                            친구_XXX_생일.getRepeat().getFinish(),
                            친구_XXX_생일.getAlarm(),
                            친구_XXX_생일.getTime(),
                            친구_XXX_생일.getLink(),
                            친구_XXX_생일.getLocation(),
                            친구_XXX_생일.getMemo()
                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL),
                    status().isOk(),
                    successDocsWithAccessToken("ScheduleApi/ForAlarm", createHttpSpecSnippets(
                            responseFields(
                                    body("result[].id", "일정 ID(PK)"),
                                    body("result[].relation.id", "관계 ID(PK)"),
                                    body("result[].relation.name", "관계 이름"),
                                    body("result[].relation.group.id", "그룹 ID(PK)"),
                                    body("result[].relation.group.name", "그룹명"),
                                    body("result[].day", "날짜"),
                                    body("result[].event", "행사 종류"),
                                    body("result[].repeatType", "반복 타입", "Nullable"),
                                    body("result[].repeatFinish", "반복 종료 날짜", "Nullable"),
                                    body("result[].alarm", "알람", "Nullable"),
                                    body("result[].time", "시간", "Nullable"),
                                    body("result[].link", "링크", "Nullable"),
                                    body("result[].location", "위치", "Nullable"),
                                    body("result[].memo", "메모", "Nullable")
                            )
                    ))
            );
        }
    }
}
