package ac.dnd.bookkeeping.server.acceptance.auth;

import io.restassured.response.ValidatableResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static ac.dnd.bookkeeping.server.acceptance.CommonRequestFixture.postRequestWithRefreshToken;

public class AuthAcceptanceStep {
    public static ValidatableResponse 토큰을_재발급받는다(final String refreshToken) {
        final String uri = UriComponentsBuilder
                .fromPath("/api/token/reissue")
                .build()
                .toUri()
                .getPath();

        return postRequestWithRefreshToken(uri, refreshToken);
    }
}
