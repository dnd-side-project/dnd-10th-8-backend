package ac.dnd.bookkeeping.server.member.presentation;

import ac.dnd.bookkeeping.server.common.ControllerTest;
import ac.dnd.bookkeeping.server.member.application.usecase.ManageAccountUseCase;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.query;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.successDocs;
import static ac.dnd.bookkeeping.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member -> ManageAccountApiController 테스트")
class ManageAccountApiControllerTest extends ControllerTest {
    @Autowired
    private ManageAccountUseCase manageAccountUseCase;

    @Nested
    @DisplayName("닉네임 중복 체크 API [GET /api/v1/check-nickname]")
    class CheckNickname {
        private static final String BASE_URL = "/api/v1/check-nickname";

        @Test
        @DisplayName("닉네임 사용 가능 여부를 조회한다")
        void success() {
            // given
            given(manageAccountUseCase.isUniqueNickname(any())).willReturn(true);

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
    @DisplayName("사용자 탈퇴 API [DELETE /api/v1/members/me]")
    class Delete {
        private static final String BASE_URL = "/api/v1/members/me";
        private final Member member = MEMBER_1.toDomain().apply(1L);

        @Test
        @DisplayName("온보딩 후 추가 정보를 기입한다")
        void success() {
            // given
            applyToken(true, member.getId());

            // when - then
            successfulExecute(
                    deleteRequestWithAccessToken(BASE_URL),
                    status().isNoContent(),
                    successDocsWithAccessToken("MemberApi/Delete")
            );
        }
    }
}
