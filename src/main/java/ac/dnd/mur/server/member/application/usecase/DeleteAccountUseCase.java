package ac.dnd.mur.server.member.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import ac.dnd.mur.server.member.domain.service.DeleteMemberRelatedResourceProcessor;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteAccountUseCase {
    private final MemberRepository memberRepository;
    private final DeleteMemberRelatedResourceProcessor deleteMemberRelatedResourceProcessor;

    @MurWritableTransactional
    public void invoke(final long memberId) {
        final Member member = memberRepository.getById(memberId);
        deleteMemberRelatedResourceProcessor.invoke(member.getId());
        memberRepository.delete(memberId);
    }
}
