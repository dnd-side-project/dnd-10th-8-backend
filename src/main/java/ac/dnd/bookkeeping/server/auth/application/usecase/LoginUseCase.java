package ac.dnd.bookkeeping.server.auth.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    public AuthToken invoke(final LoginCommand command) {
        final Member member = memberRepository.findByPlatformSocialId(command.platform().getSocialId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        return tokenIssuer.provideAuthorityToken(member.getId());
    }
}
