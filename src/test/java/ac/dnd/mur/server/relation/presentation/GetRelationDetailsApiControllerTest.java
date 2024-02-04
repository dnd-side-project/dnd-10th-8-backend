package ac.dnd.mur.server.relation.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.application.usecase.GetRelationDetailsUseCase;
import ac.dnd.mur.server.relation.application.usecase.query.response.MultipleRelationDetails;
import ac.dnd.mur.server.relation.application.usecase.query.response.SingleRelationDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Relation -> GetRelationDetailsApiController 테스트")
class GetRelationDetailsApiControllerTest extends ControllerTest {
    @Autowired
    private GetRelationDetailsUseCase getRelationDetailsUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("등록한 관계 단건 조회 API [GET /api/v1/relations/me/{relationId}]")
    class GetRelation {
        private static final String BASE_URL = "/api/v1/relations/me/{relationId}";

        @Test
        @DisplayName("등록한 관계에 대한 단건 정보를 조회한다")
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

    @Nested
    @DisplayName("등록한 관계 N건 조회 API [GET /api/v1/relations/me")
    class GetRelations {
        private static final String BASE_URL = "/api/v1/relations/me";

        @Test
        @DisplayName("등록한 관계에 대한 N건 정보를 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getRelationDetailsUseCase.getRelations(any())).willReturn(List.of(
                    new MultipleRelationDetails(3L, "HelloUser", new GroupResponse(4L, "직장")),
                    new MultipleRelationDetails(2L, "HelloUser", new GroupResponse(2L, "가족")),
                    new MultipleRelationDetails(1L, "HelloUser", new GroupResponse(1L, "친구"))
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), Map.of("name", "HelloUser")),
                    status().isOk(),
                    successDocs("RelationApi/Details/Multiple", createHttpSpecSnippets(
                            queryParameters(
                                    query("name", "검색할 이름", "관계 등록할 때 적용한 이름으로 조회", false)
                            ),
                            responseFields(
                                    body("result[].id", "관계 ID(PK)"),
                                    body("result[].name", "등록한 관계 이름"),
                                    body("result[].group.id", "그룹 ID(PK)"),
                                    body("result[].group.name", "그룹명")
                            )
                    ))
            );
        }
    }
}
