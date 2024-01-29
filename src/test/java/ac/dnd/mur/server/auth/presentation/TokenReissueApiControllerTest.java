package ac.dnd.mur.server.auth.presentation;

import ac.dnd.mur.server.auth.application.usecase.ReissueTokenUseCase;
import ac.dnd.mur.server.auth.domain.model.AuthToken;
import ac.dnd.mur.server.auth.exception.AuthExceptionCode;
import ac.dnd.mur.server.common.ControllerTest;
import ac.dnd.mur.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.failureDocsWithRefreshToken;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.successDocsWithRefreshToken;
import static ac.dnd.mur.server.common.utils.TokenUtils.ACCESS_TOKEN;
import static ac.dnd.mur.server.common.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth -> TokenReissueApiController 테스트")
class TokenReissueApiControllerTest extends ControllerTest {
    @Autowired
    private ReissueTokenUseCase reissueTokenUseCase;

    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Nested
    @DisplayName("토큰 재발급 API [POST /api/v1/token/reissue]")
    class ReissueToken {
        private static final String BASE_URL = "/api/v1/token/reissue";

        @Test
        @DisplayName("유효하지 않은 RefreshToken으로 인해 토큰 재발급에 실패한다")
        void throwExceptionByExpiredRefreshToken() {
            // given
            applyToken(false, member.getId());

            // when - then
            failedExecute(
                    postRequestWithRefreshToken(BASE_URL),
                    status().isUnauthorized(),
                    ExceptionSpec.of(AuthExceptionCode.INVALID_TOKEN),
                    failureDocsWithRefreshToken("TokenReissueApi/Failure")
            );
        }

        @Test
        @DisplayName("사용자 소유의 RefreshToken을 통해서 AccessToken과 RefreshToken을 재발급받는다")
        void success() {
            // given
            applyToken(true, member.getId());
            given(reissueTokenUseCase.invoke(any())).willReturn(new AuthToken(ACCESS_TOKEN, REFRESH_TOKEN));

            // when - then
            successfulExecute(
                    postRequestWithRefreshToken(BASE_URL),
                    status().isOk(),
                    successDocsWithRefreshToken("TokenReissueApi/Success", createHttpSpecSnippets(
                            responseFields(
                                    body("accessToken", "Access Token"),
                                    body("refreshToken", "Refresh Token")
                            )
                    ))
            );
        }
    }
}
