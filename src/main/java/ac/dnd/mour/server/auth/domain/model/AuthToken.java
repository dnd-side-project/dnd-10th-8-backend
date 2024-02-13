package ac.dnd.mour.server.auth.domain.model;

public record AuthToken(
        String accessToken,
        String refreshToken
) {
    public static final String TOKEN_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Token-Refresh";
}
