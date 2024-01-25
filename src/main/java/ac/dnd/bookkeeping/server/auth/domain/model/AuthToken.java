package ac.dnd.bookkeeping.server.auth.domain.model;

public record AuthToken(
        String accessToken,
        String refreshToken
) {
}
