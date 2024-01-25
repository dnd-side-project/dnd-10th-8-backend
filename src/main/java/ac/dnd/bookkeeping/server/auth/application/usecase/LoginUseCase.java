package ac.dnd.bookkeeping.server.auth.application.usecase;

import ac.dnd.bookkeeping.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.bookkeeping.server.auth.application.usecase.command.response.LoginResponse;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.auth.domain.service.TokenIssuer;
import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.global.annotation.WritableTransactional;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {
    private final MemberRepository memberRepository;
    private final TokenIssuer tokenIssuer;

    @WritableTransactional
    public LoginResponse invoke(final LoginCommand command) {
        final Optional<Member> member = memberRepository.findByPlatformSocialId(command.platform().getSocialId());
        if (member.isEmpty()) {
            return createMemberAndIssueToken(command);
        }
        return issueToken(member.get(), command.platform());
    }

    private LoginResponse createMemberAndIssueToken(final LoginCommand command) {
        final Member member = memberRepository.save(command.toDomain());
        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return LoginResponse.of(true, member, token);
    }

    private LoginResponse issueToken(final Member member, final SocialPlatform platform) {
        member.syncEmail(platform.getEmail());
        final AuthToken token = tokenIssuer.provideAuthorityToken(member.getId());
        return LoginResponse.of(false, member, token);
    }
}
