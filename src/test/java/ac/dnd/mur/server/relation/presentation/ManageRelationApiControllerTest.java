package ac.dnd.mur.server.relation.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.application.usecase.CreateRelationUseCase;
import ac.dnd.mur.server.relation.application.usecase.DeleteRelationUseCase;
import ac.dnd.mur.server.relation.application.usecase.UpdateRelationUseCase;
import ac.dnd.mur.server.relation.presentation.dto.request.CreateRelationRequest;
import ac.dnd.mur.server.relation.presentation.dto.request.UpdateRelationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.RelationFixture.친구_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Relation -> ManageRelationApiController 테스트")
class ManageRelationApiControllerTest extends ControllerTest {
    @Autowired
    private CreateRelationUseCase createRelationUseCase;

    @Autowired
    private UpdateRelationUseCase updateRelationUseCase;

    @Autowired
    private DeleteRelationUseCase deleteRelationUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("관계 생성 API [POST /api/v1/relations]")
    class Create {
        private static final String BASE_URL = "/api/v1/relations";
        private final CreateRelationRequest request = new CreateRelationRequest(
                1L,
                친구_1.getName(),
                친구_1.getImageUrl(),
                친구_1.getMemo()
        );

        @Test
        @DisplayName("관계를 생성한다")
        void success() {
            // given
            applyToken(true, member.getId());
            given(createRelationUseCase.invoke(any())).willReturn(1L);

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL, request),
                    status().isOk(),
                    successDocsWithAccessToken("RelationApi/Create", createHttpSpecSnippets(
                            requestFields(
                                    body("groupId", "그룹 ID(PK)", true),
                                    body("name", "이름", true),
                                    body("imageUrl", "이미지 URL", false),
                                    body("memo", "메모", false)
                            ),
                            responseFields(
                                    body("result", "생성한 관계 ID(PK)")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("관계 수정 API [PATCH /api/v1/relations/{relationId}]")
    class Update {
        private static final String BASE_URL = "/api/v1/relations/{relationId}";
        private final UpdateRelationRequest request = new UpdateRelationRequest(
                1L,
                친구_1.getName(),
                친구_1.getImageUrl(),
                친구_1.getMemo()
        );

        @Test
        @DisplayName("관계를 수정한다")
        void success() {
            // given
            applyToken(true, member.getId());
            doNothing()
                    .when(updateRelationUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    patchRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L), request),
                    status().isNoContent(),
                    successDocsWithAccessToken("RelationApi/Update", createHttpSpecSnippets(
                            pathParameters(
                                    path("relationId", "관계 ID(PK)", true)
                            ),
                            requestFields(
                                    body("groupId", "그룹 ID(PK)", true),
                                    body("name", "이름", true),
                                    body("imageUrl", "이미지 URL", false),
                                    body("memo", "메모", false)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("관계 삭제 API [DELETE /api/v1/relations/{relationId}]")
    class Delete {
        private static final String BASE_URL = "/api/v1/relations/{relationId}";

        @Test
        @DisplayName("관계를 삭제한다")
        void success() {
            // given
            applyToken(true, member.getId());
            doNothing()
                    .when(deleteRelationUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isNoContent(),
                    successDocsWithAccessToken("RelationApi/Delete", createHttpSpecSnippets(
                            pathParameters(
                                    path("relationId", "관계 ID(PK)", true)
                            )
                    ))
            );
        }
    }
}
