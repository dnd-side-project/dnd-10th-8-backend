package ac.dnd.mur.server.group.presentation;

import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.group.application.usecase.AddGroupUseCase;
import ac.dnd.mur.server.group.application.usecase.GetMemberGroupUseCase;
import ac.dnd.mur.server.group.application.usecase.RemoveGroupUseCase;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.group.exception.GroupException;
import ac.dnd.mur.server.group.presentation.dto.request.AddGroupRequest;
import ac.dnd.mur.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.path;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.failureDocsWithAccessToken;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Group -> ManageGroupApiController 테스트")
class ManageGroupApiControllerTest extends ControllerTest {
    @Autowired
    private AddGroupUseCase addGroupUseCase;

    @Autowired
    private RemoveGroupUseCase removeGroupUseCase;

    @Autowired
    private GetMemberGroupUseCase getMemberGroupUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("그룹 추가 API [POST /api/v1/groups]")
    class AddGroup {
        private static final String BASE_URL = "/api/v1/groups";

        @Test
        @DisplayName("중복된 그룹은 추가할 수 없다")
        void throwExceptionByGroupAlreadyExists() {
            // given
            applyToken(true, member.getId());
            doThrow(new GroupException(GROUP_ALREADY_EXISTS))
                    .when(addGroupUseCase)
                    .invoke(any());

            // when - then
            failedExecute(
                    postRequestWithAccessToken(BASE_URL, new AddGroupRequest("거래처")),
                    status().isConflict(),
                    ExceptionSpec.of(GROUP_ALREADY_EXISTS),
                    failureDocsWithAccessToken("GroupApi/Add/Failure", createHttpSpecSnippets(
                            requestFields(
                                    body("name", "그룹명", true)
                            )
                    ))
            );
        }

        @Test
        @DisplayName("그룹을 추가한다")
        void success() {
            // given
            applyToken(true, member.getId());
            given(addGroupUseCase.invoke(any())).willReturn(1L);

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL, new AddGroupRequest("거래처")),
                    status().isOk(),
                    successDocsWithAccessToken("GroupApi/Add/Success", createHttpSpecSnippets(
                            requestFields(
                                    body("name", "그룹명", true)
                            ),
                            responseFields(
                                    body("result", "추가한 그룹 ID(PK)")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("그룹 삭제 API [DELETE /api/v1/groups/{groupId}]")
    class RemoveGroup {
        private static final String BASE_URL = "/api/v1/groups/{groupId}";

        @Test
        @DisplayName("그룹을 삭제한다")
        void success() {
            // given
            applyToken(true, member.getId());
            doNothing()
                    .when(removeGroupUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(new UrlWithVariables(BASE_URL, 1L)),
                    status().isNoContent(),
                    successDocsWithAccessToken("GroupApi/Remove", createHttpSpecSnippets(
                            pathParameters(
                                    path("groupId", "그룹 ID(PK)", true)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("사용자가 관리하고 있는 그룹 API [GET /api/v1/groups/me]")
    class GetMemberGroups {
        private static final String BASE_URL = "/api/v1/groups/me";

        @Test
        @DisplayName("사용자가 관리하고 있는 그룹을 조회한다")
        void success() {
            // given
            applyToken(true, member.getId());
            given(getMemberGroupUseCase.invoke(member.getId())).willReturn(List.of(
                    new GroupResponse(1L, "친구"),
                    new GroupResponse(2L, "가족"),
                    new GroupResponse(3L, "지인"),
                    new GroupResponse(4L, "직장")
            ));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL),
                    status().isOk(),
                    successDocs("GroupApi/GetMembers", createHttpSpecSnippets(
                            responseFields(
                                    body("result[].id", "그룹 ID(PK)"),
                                    body("result[].name", "그룹명")
                            )
                    ))
            );
        }
    }
}