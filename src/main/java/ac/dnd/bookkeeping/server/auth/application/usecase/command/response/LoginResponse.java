package ac.dnd.bookkeeping.server.auth.application.usecase.command.response;

import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;

public record LoginResponse(
        boolean isNew,
        String accessToken,
        String refreshToken
) {
    public static LoginResponse of(final boolean isNew, final AuthToken token) {
        return new LoginResponse(isNew, token.accessToken(), token.refreshToken());
    }
}
