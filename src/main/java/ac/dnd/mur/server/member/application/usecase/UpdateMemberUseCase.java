package ac.dnd.mur.server.member.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.application.usecase.command.UpdateMemberCommand;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateMemberUseCase {
    private final MemberRepository memberRepository;

    @MurWritableTransactional
    public void invoke(final UpdateMemberCommand command) {
        final Member member = memberRepository.getById(command.id());
        member.update(command.profileImageUrl(), command.nickname(), command.gender(), command.birth());
    }
}
