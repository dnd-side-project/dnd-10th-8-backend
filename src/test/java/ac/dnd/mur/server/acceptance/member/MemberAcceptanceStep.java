package ac.dnd.mur.server.acceptance.member;

import ac.dnd.mur.server.member.presentation.dto.request.RegisterMemberRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mur.server.acceptance.CommonRequestFixture.getRequest;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequest;
import static ac.dnd.mur.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;

public class MemberAcceptanceStep {
    public static ValidatableResponse 닉네임_중복_체크를_진행한다(final String nickname) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/check-nickname?nickname={nickname}")
                .build(nickname)
                .getPath();

        return getRequest(uri);
    }

    public static ValidatableResponse 회원가입을_진행한다(final RegisterMemberRequest request) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members")
                .build()
                .toUri()
                .getPath();

        return postRequest(uri, request);
    }

    public static ValidatableResponse 서비스_탈퇴를_진행한다(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/members/me")
                .build()
                .toUri()
                .getPath();

        return postRequestWithAccessToken(uri, accessToken);
    }
}
