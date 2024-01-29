package ac.dnd.mur.server.auth.application.usecase;

import ac.dnd.mur.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.auth.domain.model.AuthToken;
import ac.dnd.mur.server.auth.domain.service.TokenIssuer;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.global.annotation.WritableTransactional;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    @WritableTransactional
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
