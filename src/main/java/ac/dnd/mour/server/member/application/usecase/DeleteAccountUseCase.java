package ac.dnd.mour.server.member.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.member.domain.service.DeleteMemberRelatedResourceProcessor;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteAccountUseCase {
    private final MemberRepository memberRepository;
    private final DeleteMemberRelatedResourceProcessor deleteMemberRelatedResourceProcessor;

    @MourWritableTransactional
    public void invoke(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        deleteMemberRelatedResourceProcessor.invoke(member.getId());
        memberRepository.delete(memberId);
    }
}
