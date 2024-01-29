package ac.dnd.mur.server.auth.domain.service;

import ac.dnd.mur.server.auth.application.adapter.TokenStore;
import ac.dnd.mur.server.auth.domain.model.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenIssuer {
    private final TokenProvider tokenProvider;
    private final TokenStore tokenStore;

    public AuthToken provideAuthorityToken(final long memberId) {
        final AuthToken token = createToken(memberId);
        tokenStore.synchronizeRefreshToken(memberId, token.refreshToken());
        return token;
    }

    public AuthToken reissueAuthorityToken(final long memberId) {
        final AuthToken token = createToken(memberId);
        tokenStore.updateRefreshToken(memberId, token.refreshToken());
        return token;
    }

    private AuthToken createToken(final long memberId) {
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final String refreshToken = tokenProvider.createRefreshToken(memberId);
        return new AuthToken(accessToken, refreshToken);
    }

    public boolean isMemberRefreshToken(final long memberId, final String refreshToken) {
        return tokenStore.isMemberRefreshToken(memberId, refreshToken);
    }
}
