package ac.dnd.mur.server.auth.application.usecase;

import ac.dnd.mur.server.auth.application.adapter.TokenStore;
import ac.dnd.mur.server.auth.application.usecase.command.LogoutCommand;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LogoutUseCase {
    private final MemberRepository memberRepository;
    private final TokenStore tokenStore;

    public void invoke(final LogoutCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        tokenStore.deleteRefreshToken(member.getId());
    }
}
