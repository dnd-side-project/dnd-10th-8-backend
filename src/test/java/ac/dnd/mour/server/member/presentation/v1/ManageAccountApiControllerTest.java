package ac.dnd.mour.server.member.presentation.v1;

import ac.dnd.mour.server.common.ControllerTest;
import ac.dnd.mour.server.member.application.usecase.DeleteAccountUseCase;
import ac.dnd.mour.server.member.application.usecase.UpdateMemberUseCase;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.presentation.v1.request.UpdateMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member -> ManageAccountApiController 테스트")
class ManageAccountApiControllerTest extends ControllerTest {
    @Autowired
    private UpdateMemberUseCase updateMemberUseCase;

    @Autowired
    private DeleteAccountUseCase deleteAccountUseCase;

    @Nested
    @DisplayName("사용자 정보 수정 API [GET /api/v1/members/me]")
    class Update {
        private static final String BASE_URL = "/api/v1/members/me";
        private final UpdateMemberRequest request = new UpdateMemberRequest(
                MEMBER_1.getProfileImageUrl(),
                MEMBER_1.getNickname().getValue(),
                MEMBER_1.getGender().getValue(),
                MEMBER_1.getBirth()
        );

        @Test
        @DisplayName("사용자 정보를 수정한다")
        void success() {
            // given
            doNothing()
                    .when(updateMemberUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    patchRequestWithAccessToken(BASE_URL, request),
                    status().isNoContent(),
                    successDocsWithAccessToken("MemberApi/Update", createHttpSpecSnippets(
                            requestFields(
                                    body("profileImageUrl", "프로필 이미지 URL", true),
                                    body("nickname", "닉네임", true),
                                    body("gender", "성별", "male / female", true),
                                    body("birth", "생년월일", "yyyy-MM-dd", true)
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("사용자 탈퇴 API [DELETE /api/v1/members/me]")
    class Delete {
        private static final String BASE_URL = "/api/v1/members/me";
        private final Member member = MEMBER_1.toDomain().apply(1L);

        @Test
        @DisplayName("탈퇴 처리를 진행한다")
        void success() {
            // given
            applyToken(true, member);
            doNothing()
                    .when(deleteAccountUseCase)
                    .invoke(anyLong());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(BASE_URL),
                    status().isNoContent(),
                    successDocsWithAccessToken("MemberApi/Delete")
            );
        }
    }
}
