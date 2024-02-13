package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.application.usecase.command.UpdateMemberCommand;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateMemberUseCase {
    private final MemberRepository memberRepository;

    @MourWritableTransactional
    public void invoke(final UpdateMemberCommand command) {
        final Member member = memberRepository.getById(command.id());
        member.update(command.profileImageUrl(), command.nickname(), command.gender(), command.birth());
    }
}
