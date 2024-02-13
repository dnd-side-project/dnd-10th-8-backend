package ac.dnd.mour.server.acceptance.auth;

import ac.dnd.mour.server.auth.presentation.dto.request.LoginRequest;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequest;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequestWithAccessToken;
import static ac.dnd.mour.server.acceptance.CommonRequestFixture.postRequestWithRefreshToken;

public class AuthAcceptanceStep {
    public static ValidatableResponse 로그인을_진행한다(final String socialId, final String email) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/auth/login")
                .build()
                .toUri()
                .getPath();

        final LoginRequest request = new LoginRequest(socialId, email);

        return postRequest(uri, request);
    }

    public static ValidatableResponse 로그아웃을_진행한다(final String accessToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/auth/logout")
                .build()
                .toUri()
                .getPath();

        return postRequestWithAccessToken(uri, accessToken);
    }

    public static ValidatableResponse 토큰을_재발급받는다(final String refreshToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/v1/token/reissue")
                .build()
                .toUri()
                .getPath();

        return postRequestWithRefreshToken(uri, refreshToken);
    }
}
