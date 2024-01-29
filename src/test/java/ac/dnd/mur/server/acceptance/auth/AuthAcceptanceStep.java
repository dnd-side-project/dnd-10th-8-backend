package ac.dnd.mur.server.acceptance.auth;

import ac.dnd.mur.server.acceptance.CommonRequestFixture;
import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

public class AuthAcceptanceStep {
    public static ValidatableResponse 토큰을_재발급받는다(final String refreshToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/token/reissue")
                .build()
                .toUri()
                .getPath();

        return CommonRequestFixture.postRequestWithRefreshToken(uri, refreshToken);
    }
}
