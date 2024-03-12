package ac.dnd.mour.server.heart.presentation.v1;

import ac.dnd.mour.server.common.ControllerTest;
import ac.dnd.mour.server.heart.application.usecase.ApplyUnrecordedHeartUseCase;
import ac.dnd.mour.server.heart.application.usecase.CreateHeartUseCase;
import ac.dnd.mour.server.heart.application.usecase.DeleteHeartUseCase;
import ac.dnd.mour.server.heart.application.usecase.UpdateHeartUseCase;
import ac.dnd.mour.server.heart.presentation.v1.request.ApplyUnrecordedHeartRequest;
import ac.dnd.mour.server.heart.presentation.v1.request.CreateHeartRequest;
import ac.dnd.mour.server.heart.presentation.v1.request.UpdateHeartRequest;
import ac.dnd.mour.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mour.server.common.fixture.HeartFixture.결혼_축의금을_보냈다;
import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
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

@DisplayName("Heart -> ManageHeartApiController 테스트")
class ManageHeartApiControllerTest extends ControllerTest {
    @Autowired
    private CreateHeartUseCase createHeartUseCase;

    @Autowired
    private ApplyUnrecordedHeartUseCase applyUnrecordedHeartUseCase;

    @Autowired
    private UpdateHeartUseCase updateHeartUseCase;

    @Autowired
    private DeleteHeartUseCase deleteHeartUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("마음 생성 API [POST /api/v1/hearts]")
    class Create {
        private static final String BASE_URL = "/api/v1/hearts";
        private final CreateHeartRequest request = new CreateHeartRequest(
                1L,
                결혼_축의금을_보냈다.isGive(),
                결혼_축의금을_보냈다.getMoney(),
                결혼_축의금을_보냈다.getDay(),
                결혼_축의금을_보냈다.getEvent(),
                결혼_축의금을_보냈다.getMemo(),
                결혼_축의금을_보냈다.getTags()
        );

        @Test
        @DisplayName("마음을 생성한다")
        void success() {
            // given
            applyToken(true, member);
            given(createHeartUseCase.invoke(any())).willReturn(1L);

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL, request),
                    status().isOk(),
                    successDocsWithAccessToken("HeartApi/Create", createHttpSpecSnippets(
                            requestFields(
                                    body("relationId", "관계 ID(PK)", true),
                                    body("give", "주고 받음 여부", "true=보낸 마음, false=받은 마음", true),
                                    body("money", "금액", true),
                                    body("day", "날짜", true),
                                    body("event", "행사 종류", true),
                                    body("memo", "메모", false),
                                    body("tags", "태그", "0..N개", false)
                            ),
                            responseFields(
                                    body("result", "생성한 마음 ID(PK)")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("지출(보낸 마음)이 기록되지 않은 일정에 대한 마음 생성 API [POST /api/v1/hearts/unrecorded-schedule]")
    class ApplyUnrecordedHeart {
        private static final String BASE_URL = "/api/v1/hearts/unrecorded-schedule";
        private final ApplyUnrecordedHeartRequest request = new ApplyUnrecordedHeartRequest(
                1L,
                결혼_축의금을_보냈다.getMoney(),
                결혼_축의금을_보냈다.getTags()
        );

        @Test
        @DisplayName("지출(보낸 마음)이 기록되지 않은 일정에 대한 마음을 생성한다")
        void success() {
            // given
            applyToken(true, member);
            given(applyUnrecordedHeartUseCase.invoke(any())).willReturn(1L);

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL, request),
                    status().isOk(),
                    successDocsWithAccessToken("HeartApi/ApplyUnrecordedHeart", createHttpSpecSnippets(
                            requestFields(
                                    body("scheduleId", "일정 ID(PK)", true),
                                    body("money", "금액", true),
                                    body("tags", "태그", "0..N개", false)
                            ),
                            responseFields(
                                    body("result", "생성한 마음 ID(PK)")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("마음 수정 API [PATCH /api/v1/hearts/{heartId}]")
    class Update {
        private static final String BASE_URL = "/api/v1/hearts/{heartId}";
        private final UpdateHeartRequest request = new UpdateHeartRequest(
                결혼_축의금을_보냈다.getMoney(),
                결혼_축의금을_보냈다.getDay(),
                결혼_축의금을_보냈다.getEvent(),
                결혼_축의금을_보냈다.getMemo(),
                결혼_축의금을_보냈다.getTags()
        );

        @Test
        @DisplayName("마음을 수정한다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(updateHeartUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    patchRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), request),
                    status().isNoContent(),
                    successDocsWithAccessToken("HeartApi/Update", createHttpSpecSnippets(
                            pathParameters(
                                    path("heartId", "마음 ID(PK)", true)
                            ),
                            requestFields(
                                    body("money", "금액", true),
                                    body("day", "날짜", true),
                                    body("event", "행사 종류", true),
                                    body("memo", "메모", false),
                                    body("tags", "태그", "0..N개", false)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("마음 삭제 API [DELETE /api/v1/hearts/{heartId}]")
    class Delete {
        private static final String BASE_URL = "/api/v1/hearts/{heartId}";

        @Test
        @DisplayName("마음을 삭제한다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(deleteHeartUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isNoContent(),
                    successDocsWithAccessToken("HeartApi/Delete", createHttpSpecSnippets(
                            pathParameters(
                                    path("heartId", "마음 ID(PK)", true)
                            )
                    ))
            );
        }
    }
}
