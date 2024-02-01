package ac.dnd.mur.server.member.presentation;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.member.application.usecase.DeleteAccountUseCase;
import ac.dnd.mur.server.member.application.usecase.ManageResourceUseCase;
import ac.dnd.mur.server.member.application.usecase.RegisterAccountUseCase;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.presentation.dto.request.RegisterMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static ac.dnd.mur.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.mur.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member -> ManageAccountApiController 테스트")
class ManageAccountApiControllerTest extends ControllerTest {
    @Autowired
    private ManageResourceUseCase manageResourceUseCase;

    @Autowired
    private RegisterAccountUseCase registerAccountUseCase;

    @Autowired
    private DeleteAccountUseCase deleteAccountUseCase;

    @Nested
    @DisplayName("닉네임 중복 체크 API [GET /api/v1/members/check-nickname]")
    class CheckNickname {
        private static final String BASE_URL = "/api/v1/members/check-nickname";

        @Test
        @DisplayName("닉네임 사용 가능 여부를 조회한다")
        void success() {
            // given
            given(manageResourceUseCase.isUniqueNickname(any())).willReturn(true);

            // when - then
            successfulExecute(
                    getRequest(BASE_URL, Map.of("nickname", "Hello")),
                    status().isOk(),
                    successDocs("MemberApi/Register/CheckNickname", createHttpSpecSnippets(
                            queryParameters(
                                    query("nickname", "중복 체크할 닉네임", true)
                            ),
                            responseFields(
                                    body("result", "닉네임 사용 가능 여부")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("회원가입 + 로그인 처리 API [GET /api/v1/members]")
    class Register {
        private static final String BASE_URL = "/api/v1/members";
        private final RegisterMemberRequest request = new RegisterMemberRequest(
                MEMBER_1.getPlatform().getSocialId(),
                MEMBER_1.getPlatform().getEmail().getValue(),
                MEMBER_1.getProfileImageUrl(),
                MEMBER_1.getName(),
                MEMBER_1.getNickname().getValue(),
                MEMBER_1.getGender().getValue(),
                MEMBER_1.getBirth()
        );

        @Test
        @DisplayName("회원가입 + 로그인 처리를 진행한다")
        void success() {
            // given
            given(registerAccountUseCase.register(any())).willReturn(new AuthMember(1L, ACCESS_TOKEN, REFRESH_TOKEN));

            // when - then
            successfulExecute(
                    postRequest(BASE_URL, request),
                    status().isOk(),
                    successDocs("MemberApi/Register/Process", createHttpSpecSnippets(
                            requestFields(
                                    body("socialId", "소셜 플랫폼 ID", true),
                                    body("email", "소셜 플랫폼 이메일", true),
                                    body("profileImageUrl", "소셜 플랫폼 프로필 이미지 URL", true),
                                    body("name", "소셜 플랫폼 사용자 이름", true),
                                    body("nickname", "닉네임", true),
                                    body("gender", "성별", "male / female", true),
                                    body("birth", "생년월일", "yyyy-MM-dd", true)
                            ),
                            responseFields(
                                    body("id", "사용자 ID (PK)"),
                                    body("accessToken", "Access Token"),
                                    body("refreshToken", "Refresh Token")
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
            applyToken(true, member.getId());
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
