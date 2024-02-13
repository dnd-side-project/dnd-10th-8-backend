package ac.dnd.mour.server.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static ac.dnd.mour.server.auth.domain.model.AuthToken.ACCESS_TOKEN_HEADER;
import static ac.dnd.mour.server.auth.domain.model.AuthToken.REFRESH_TOKEN_HEADER;
import static ac.dnd.mour.server.auth.domain.model.AuthToken.TOKEN_TYPE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenExtractor {
    public static Optional<String> extractAccessToken(final HttpServletRequest request) {
        final String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if (isEmptyToken(token)) {
            return Optional.empty();
        }
        return checkToken(token.split(" "));
    }

    public static Optional<String> extractRefreshToken(final HttpServletRequest request) {
        final String token = request.getHeader(REFRESH_TOKEN_HEADER);
        if (isEmptyToken(token)) {
            return Optional.empty();
        }
        return checkToken(token.split(" "));
    }

    private static boolean isEmptyToken(final String token) {
        return !StringUtils.hasText(token);
    }

    private static Optional<String> checkToken(final String[] parts) {
        if (parts.length == 2 && parts[0].equals(TOKEN_TYPE)) {
            return Optional.ofNullable(parts[1]);
        }
        return Optional.empty();
    }
}
