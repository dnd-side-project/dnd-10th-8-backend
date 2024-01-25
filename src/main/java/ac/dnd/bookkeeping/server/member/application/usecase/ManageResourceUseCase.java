package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.global.annotation.WritableTransactional;
import ac.dnd.bookkeeping.server.member.application.usecase.command.CompleteInfoCommand;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ManageResourceUseCase {
    private final MemberRepository memberRepository;

    @WritableTransactional
    public void completeInfo(final CompleteInfoCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        member.complete(command.nickname(), command.gender(), command.birth());
    }
}
