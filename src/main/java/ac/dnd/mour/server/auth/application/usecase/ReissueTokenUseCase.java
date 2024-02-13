package ac.dnd.mour.server.auth.application.usecase;

import ac.dnd.mour.server.auth.application.usecase.command.ReissueTokenCommand;
import ac.dnd.mour.server.auth.domain.model.AuthToken;
import ac.dnd.mour.server.auth.domain.service.TokenIssuer;
import ac.dnd.mour.server.auth.domain.service.TokenProvider;
import ac.dnd.mour.server.auth.exception.AuthException;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.auth.exception.AuthExceptionCode.INVALID_TOKEN;

@UseCase
@RequiredArgsConstructor
public class ReissueTokenUseCase {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final TokenIssuer tokenIssuer;

    public AuthToken invoke(final ReissueTokenCommand command) {
        final Member member = getMember(command.refreshToken());
        validateMemberToken(member.getId(), command.refreshToken());
        return tokenIssuer.reissueAuthorityToken(member.getId());
    }

    private Member getMember(final String refreshToken) {
        final long memberId = tokenProvider.getId(refreshToken);
        return memberRepository.getById(memberId);
    }

    private void validateMemberToken(final long memberId, final String refreshToken) {
        if (isAnonymousRefreshToken(memberId, refreshToken)) {
            throw new AuthException(INVALID_TOKEN);
        }
    }

    private boolean isAnonymousRefreshToken(final long memberId, final String refreshToken) {
        return !tokenIssuer.isMemberRefreshToken(memberId, refreshToken);
    }
}
