package ac.dnd.bookkeeping.server.auth.presentation;

import ac.dnd.bookkeeping.server.auth.application.usecase.LoginUseCase;
import ac.dnd.bookkeeping.server.auth.application.usecase.LogoutUseCase;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.presentation.dto.request.LoginRequest;
import ac.dnd.bookkeeping.server.common.ControllerTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.failureDocs;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.bookkeeping.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth -> AuthApiController 테스트")
class AuthApiControllerTest extends ControllerTest {
    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private LogoutUseCase logoutUseCase;

    @Nested
    @DisplayName("로그인 API [POST /api/v1/auth/login]")
    class Login {
        private static final String BASE_URL = "/api/v1/auth/login";
        private final LoginRequest request = new LoginRequest(
                MEMBER_1.getPlatform().getSocialId(),
                MEMBER_1.getPlatform().getEmail().getValue()
        );

        @Test
        @DisplayName("DB에 존재하지 않는 사용자라면 예외가 발생하고 회원가입 플로우로 이동한다")
        void throwExceptionByMemberNotFound() {
            // given
            doThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND))
                    .when(loginUseCase)
                    .invoke(any());

            // when - then
            failedExecute(
                    postRequest(BASE_URL, request),
                    status().isNotFound(),
                    ExceptionSpec.of(MemberExceptionCode.MEMBER_NOT_FOUND),
                    failureDocs("AuthApi/Login/Failure", createHttpSpecSnippets(
                            requestFields(
                                    body("socialId", "소셜 플랫폼 ID", true),
                                    body("email", "소셜 플랫폼 이메일", true)
                            )
                    ))
            );
        }

        @Test
        @DisplayName("DB에 존재하는 사용자면 로그인 처리를 진행하고 토큰을 발급한다")
        void success() {
            // given
            given(loginUseCase.invoke(any())).willReturn(new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN));

            // when - then
            successfulExecute(
                    postRequest(BASE_URL, request),
                    status().isOk(),
                    successDocs("AuthApi/Login/Success", createHttpSpecSnippets(
                            requestFields(
                                    body("socialId", "소셜 플랫폼 ID", true),
                                    body("email", "소셜 플랫폼 이메일", true)
                            ),
                            responseFields(
                                    body("accessToken", "Access Token"),
                                    body("refreshToken", "Refresh Token")
                            )
                    ))
            );
        }
    }

    @Nested
    @DisplayName("로그아웃 API [POST /api/v1/auth/logout]")
    class Logout {
        private static final String BASE_URL = "/api/v1/auth/logout";
        private final Member member = MEMBER_1.toDomain().apply(1L);


        @Test
        @DisplayName("로그아웃을 진행한다")
        void success() {
            // given
            applyToken(true, member.getId());
            doNothing()
                    .when(logoutUseCase)
                    .invoke(any());

            // when - then
            successfulExecute(
                    postRequestWithAccessToken(BASE_URL),
                    status().isNoContent(),
                    successDocsWithAccessToken("AuthApi/Logout")
            );
        }
    }
}
