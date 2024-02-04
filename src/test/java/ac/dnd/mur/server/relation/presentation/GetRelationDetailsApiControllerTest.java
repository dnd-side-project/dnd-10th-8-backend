package ac.dnd.mur.server.relation.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.application.usecase.GetRelationDetailsUseCase;
import ac.dnd.mur.server.relation.application.usecase.query.response.SingleRelationDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Relation -> GetRelationDetailsApiController 테스트")
class GetRelationDetailsApiControllerTest extends ControllerTest {
    @Autowired
    private GetRelationDetailsUseCase getRelationDetailsUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("등록한 단건 관계 조회 API [GET /api/v1/relations/me/{relationId}]")
    class GetRelation {
        private static final String BASE_URL = "/api/v1/relations/me/{relationId}";

        @Test
        @DisplayName("등록한 단건 관계에 대한 정보를 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getRelationDetailsUseCase.getRelation(any())).willReturn(new SingleRelationDetails(
                    1L,
                    친구_1.getName(),
                    new GroupResponse(1L, "친구"),
                    320_000,
                    250_000
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isOk(),
                    successDocs("RelationApi/Details/Single", createHttpSpecSnippets(
                            pathParameters(
                                    path("relationId", "관계 ID(PK)", true)
                            ),
                            responseFields(
                                    body("id", "관계 ID(PK)"),
                                    body("name", "등록한 관계 이름"),
                                    body("group.id", "그룹 ID(PK)"),
                                    body("group.name", "그룹명"),
                                    body("giveMoney", "보낸 금액"),
                                    body("takeMoney", "받은 금액")
                            )
                    ))
            );
        }
    }
}
