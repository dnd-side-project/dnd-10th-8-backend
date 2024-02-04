package ac.dnd.mur.server.heart.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.heart.application.usecase.GetHeartHistoryUseCase;
import ac.dnd.mur.server.heart.application.usecase.query.response.HeartHistoryDetails;
import ac.dnd.mur.server.heart.application.usecase.query.response.SpecificRelationHeartHistoryDetails;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.model.response.RelationSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static ac.dnd.mur.server.common.fixture.GroupFixture.친구;
import static ac.dnd.mur.server.common.fixture.HeartFixture.결혼_축의금을_보냈다;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Heart -> GetHeartHistoryApiController 테스트")
class GetHeartHistoryApiControllerTest extends ControllerTest {
    @Autowired
    private GetHeartHistoryUseCase getHeartHistoryUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("메인 홈 마음 내역 조회 API [GET /api/v1/hearts/me")
    class GetHeartHistories {
        private static final String BASE_URL = "/api/v1/hearts/me";

        @Test
        @DisplayName("메인 홈 마음 내역을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getHeartHistoryUseCase.getHeartHistories(any())).willReturn(List.of(
                    new HeartHistoryDetails(
                            new RelationSummary(
                                    1L,
                                    "관계-친구1",
                                    new GroupResponse(1L, "친구")
                            ),
                            7_000_000,
                            15_000_000
                    ),
                    new HeartHistoryDetails(
                            new RelationSummary(
                                    2L,
                                    "관계-친구2",
                                    new GroupResponse(1L, "친구")
                            ),
                            5_000_000,
                            10_000_000
                    )
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), Map.of("sort", "intimacy")),
                    status().isOk(),
                    successDocsWithAccessToken("HeartApi/Search/Home", createHttpSpecSnippets(
                            queryParameters(
                                    query("sort", "정렬", "- 최신 = recent" + ENTER + "- 친밀도 = intimacy", true),
                                    query("name", "이름", "관계 등록 시 적용한 이름", false)
                            ),
                            responseFields(
                                    body("result[].relation.id", "관계 ID(PK)"),
                                    body("result[].relation.name", "등록한 관계 이름"),
                                    body("result[].relation.group.id", "그룹 ID(PK)"),
                                    body("result[].relation.group.name", "그룹명"),
                                    body("result[].giveMoney", "보낸 금액"),
                                    body("result[].takeMoney", "받은 금액")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("특정 관계간에 주고받은 마음 내역 조회 API [GET /api/v1/hearts/me/{relationId}")
    class GetHeartHistoriesWithRelation {
        private static final String BASE_URL = "/api/v1/hearts/me/{relationId}";

        @Test
        @DisplayName("메인 홈 마음 내역을 조회한다")
        void success() {
            // given
            final Relation relation = 친구_1.toDomain(member, 친구.toDomain(member).apply(1L)).apply(1L);

            applyToken(true, member);
            given(getHeartHistoryUseCase.getHeartHistoriesWithRelation(any())).willReturn(List.of(
                    SpecificRelationHeartHistoryDetails.from(결혼_축의금을_보냈다.toDomain(member, relation).apply(1L))
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), Map.of("sort", "recent")),
                    status().isOk(),
                    successDocsWithAccessToken("HeartApi/Search/SpecificRelation", createHttpSpecSnippets(
                            pathParameters(
                                    path("relationId", "관계 ID(PK)", true)
                            ),
                            queryParameters(
                                    query("sort", "정렬", "- 최신 = recent" + ENTER + "- 과거 = old", true)
                            ),
                            responseFields(
                                    body("result[].id", "관계 ID(PK)"),
                                    body("result[].give", "주고 받음 여부", "true=보낸 마음, false=받은 마음"),
                                    body("result[].money", "금액"),
                                    body("result[].day", "날짜"),
                                    body("result[].event", "행사", "Nullable"),
                                    body("result[].memo", "메모", "Nullable"),
                                    body("result[].tags", "태그", "0..N개")
                            )
                    ))
            );
        }
    }
}
