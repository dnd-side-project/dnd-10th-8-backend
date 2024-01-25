package ac.dnd.bookkeeping.server.member.application.usecase;

import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import ac.dnd.bookkeeping.server.global.annotation.WritableTransactional;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteAccountUseCase {
    private final MemberRepository memberRepository;

    @WritableTransactional
    public void invoke(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        member.delete();
    }
}
