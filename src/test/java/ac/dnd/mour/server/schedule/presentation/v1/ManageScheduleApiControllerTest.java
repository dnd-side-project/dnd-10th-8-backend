package ac.dnd.mour.server.schedule.presentation.v1;

import ac.dnd.mour.server.common.ControllerTest;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.schedule.application.usecase.CreateScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.DeleteScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.HideScheduleUseCase;
import ac.dnd.mour.server.schedule.application.usecase.UpdateScheduleUseCase;
import ac.dnd.mour.server.schedule.presentation.v1.request.CreateScheduleRequest;
import ac.dnd.mour.server.schedule.presentation.v1.request.UpdateScheduleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.fixture.ScheduleFixture.결혼식;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Schedule -> ManageScheduleApiController 테스트")
class ManageScheduleApiControllerTest extends ControllerTest {
    @Autowired
    private CreateScheduleUseCase createScheduleUseCase;

    @Autowired
    private UpdateScheduleUseCase updateScheduleUseCase;

    @Autowired
    private DeleteScheduleUseCase deleteScheduleUseCase;

    @Autowired
    private HideScheduleUseCase hideScheduleUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("일정 생성 API [POST /api/v1/schedules]")
    class Create {
        private static final String BASE_URL = "/api/v1/schedules";
        private final CreateScheduleRequest request = new CreateScheduleRequest(
                1L,
                결혼식.getDay(),
                결혼식.getEvent(),
                null,
                null,
                결혼식.getAlarm(),
                결혼식.getTime(),
                결혼식.getLink(),
                결혼식.getLocation(),
                결혼식.getMemo()
        );

        @Test
        @DisplayName("일정을 생성한다")
        void success() {
            // given
            applyToken(true, member);
            given(createScheduleUseCase.invoke(any())).willReturn(1L);

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL, request),
                    status().isOk(),
                    successDocsWithAccessToken("ScheduleApi/Create", createHttpSpecSnippets(
                            requestFields(
                                    body("relationId", "관계 ID(PK)", true),
                                    body("day", "날짜", "yyyy-MM-dd", true),
                                    body("event", "행사", true),
                                    body(
                                            "repeatType",
                                            "반복 타입",
                                            "- 반복 X = 안보내도됨" + ENTER
                                                    + "- 반복 O = month or year",
                                            false
                                    ),
                                    body("repeatFinish", "반복 종료 날짜", "yyyy-MM-dd", false),
                                    body("alarm", "알람 설정", "[KST] yyyy-MM-ddTHH:mm:ss", false),
                                    body("time", "일정 시간", "[KST] HH:mm:ss", false),
                                    body("link", "링크", false),
                                    body("location", "위치", false),
                                    body("memo", "메모", false)
                            ),
                            responseFields(
                                    body("result", "생성한 일정 ID(PK)")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("일정 수정 API [PATCH /api/v1/schedules/{scheduleId}]")
    class Update {
        private static final String BASE_URL = "/api/v1/schedules/{scheduleId}";
        private final UpdateScheduleRequest request = new UpdateScheduleRequest(
                결혼식.getDay(),
                결혼식.getEvent(),
                null,
                null,
                결혼식.getAlarm(),
                결혼식.getTime(),
                결혼식.getLink(),
                결혼식.getLocation(),
                결혼식.getMemo()
        );

        @Test
        @DisplayName("일정을 수정한다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(updateScheduleUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    patchRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), request),
                    status().isNoContent(),
                    successDocsWithAccessToken("ScheduleApi/Update", createHttpSpecSnippets(
                            pathParameters(
                                    path("scheduleId", "일정 ID(PK)", true)
                            ),
                            requestFields(
                                    body("day", "날짜", "yyyy-MM-dd", true),
                                    body("event", "행사", true),
                                    body(
                                            "repeatType",
                                            "반복 타입",
                                            "- 반복 X = 안보내도됨" + ENTER
                                                    + "- 반복 O = month or year",
                                            false
                                    ),
                                    body("repeatFinish", "반복 종료 날짜", "yyyy-MM-dd", false),
                                    body("alarm", "알람 설정", "[KST] yyyy-MM-ddTHH:mm:ss", false),
                                    body("time", "일정 시간", "[KST] HH:mm:ss", false),
                                    body("link", "링크", false),
                                    body("location", "위치", false),
                                    body("memo", "메모", false)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("일정 삭제 API [DELETE /api/v1/schedules/{scheduleId}]")
    class Delete {
        private static final String BASE_URL = "/api/v1/schedules/{scheduleId}";

        @Test
        @DisplayName("일정을 삭제한다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(deleteScheduleUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isNoContent(),
                    successDocsWithAccessToken("ScheduleApi/Delete", createHttpSpecSnippets(
                            pathParameters(
                                    path("scheduleId", "일정 ID(PK)", true)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("일정 숨기기 API [PATCH /api/v1/schedules/{scheduleId}/hide]")
    class Hide {
        private static final String BASE_URL = "/api/v1/schedules/{scheduleId}/hide";

        @Test
        @DisplayName("일정을 숨긴다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(hideScheduleUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    patchRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isNoContent(),
                    successDocsWithAccessToken("ScheduleApi/Hide", createHttpSpecSnippets(
                            pathParameters(
                                    path("scheduleId", "일정 ID(PK)", true)
                            )
                    ))
            );
        }
    }
}
