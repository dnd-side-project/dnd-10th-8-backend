package ac.dnd.bookkeeping.server.auth.domain.service;

import ac.dnd.bookkeeping.server.auth.application.adapter.TokenStore;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenIssuer {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final TokenStore tokenStore;

    public AuthToken provideAuthorityToken(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        final AuthToken token = createToken(member.getId());
        tokenStore.synchronizeRefreshToken(member.getId(), token.refreshToken());
        return token;
    }

    public AuthToken reissueAuthorityToken(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        final AuthToken token = createToken(member.getId());
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

    public void deleteRefreshToken(final long memberId) {
        tokenStore.deleteRefreshToken(memberId);
    }
}
