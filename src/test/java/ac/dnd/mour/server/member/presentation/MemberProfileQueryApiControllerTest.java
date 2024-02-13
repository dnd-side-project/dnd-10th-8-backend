package ac.dnd.mour.server.member.presentation;

import ac.dnd.mour.server.common.ControllerTest;
import ac.dnd.mour.server.member.application.usecase.GetPrivateProfileUseCase;
import ac.dnd.mour.server.member.application.usecase.query.response.MemberPrivateProfile;
import ac.dnd.mour.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.body;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.createHttpSpecSnippets;
import static ac.dnd.mour.server.common.utils.RestDocsSpecificationUtils.successDocsWithAccessToken;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member -> MemberProfileQueryApiController 테스트")
class MemberProfileQueryApiControllerTest extends ControllerTest {
    @Autowired
    private GetPrivateProfileUseCase getPrivateProfileUseCase;

    @Nested
    @DisplayName("사용자 마이페이지 프로필 조회 API [GET /api/v1/members/me]")
    class GetPrivateProfile {
        private static final String BASE_URL = "/api/v1/members/me";
        private final Member member = MEMBER_1.toDomain().apply(1L);

        @Test
        @DisplayName("마이페이지 프로필을 조회한다")
        void success() {
            // given
            applyToken(true, member);
            given(getPrivateProfileUseCase.invoke(member.getId())).willReturn(MemberPrivateProfile.of(member));

            // when - then
            successfulExecute(
                    getRequestWithAccessToken(BASE_URL),
                    status().isOk(),
                    successDocsWithAccessToken("MemberApi/Profile/Private", createHttpSpecSnippets(
                            responseFields(
                                    body("id", "사용자 ID (PK)"),
                                    body("email", "이메일"),
                                    body("profileImageUrl", "프로필 이미지 URL"),
                                    body("name", "사용자 이름"),
                                    body("nickname", "닉네임"),
                                    body("gender", "성별", "male / female"),
                                    body("birth", "생년월일", "yyyy-MM-dd")
                            )
                    ))
            );
        }
    }
}
