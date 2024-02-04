package ac.dnd.mur.server.heart.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.heart.application.usecase.GetHeartHistoryUseCase;
import ac.dnd.mur.server.heart.application.usecase.query.response.HeartHistoryDetails;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Heart -> GetHeartHistoryApiController 테스트")
class GetHeartHistoryApiControllerTest extends ControllerTest {
    @Autowired
    private GetHeartHistoryUseCase getHeartHistoryUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("메인 홈 마음 내역 조회 API [GET /api/v1/hearts/me")
    class GetRelation {
        private static final String BASE_URL = "/api/v1/hearts/me";

        @Test
        @DisplayName("메인 홈 마음 내역을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getHeartHistoryUseCase.getHeartHistories(any())).willReturn(List.of(
                    new HeartHistoryDetails(
                            new RelationDetails(
                                    1L,
                                    "관계-친구1",
                                    1L,
                                    "친구"
                            ),
                            7_000_000,
                            15_000_000
                    ),
                    new HeartHistoryDetails(
                            new RelationDetails(
                                    2L,
                                    "관계-친구2",
                                    1L,
                                    "친구"
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
                                    body("result[].relation.groupId", "그룹 ID(PK)"),
                                    body("result[].relation.groupName", "그룹명"),
                                    body("result[].giveMoney", "보낸 금액"),
                                    body("result[].takeMoney", "받은 금액")
                            )
                    ))
            );
        }
    }
}
