package ac.dnd.mour.server.auth.application.usecase;

import ac.dnd.mour.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.auth.domain.model.AuthToken;
import ac.dnd.mour.server.auth.domain.service.TokenIssuer;
import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    @MourWritableTransactional
    public AuthMember invoke(final LoginCommand command) {
        final Member member = getMemberBySocialId(command);
        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return AuthMember.of(member, token);
    }

    private Member getMemberBySocialId(final LoginCommand command) {
        final Member member = memberRepository.findByPlatformSocialId(command.socialId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.syncEmail(command.email());
        return member;
    }
}
