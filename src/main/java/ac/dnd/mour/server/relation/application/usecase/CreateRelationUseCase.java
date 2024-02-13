package ac.dnd.mour.server.relation.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import ac.dnd.mour.server.relation.application.usecase.command.CreateRelationCommand;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateRelationUseCase {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final RelationRepository relationRepository;

    @MourWritableTransactional
    public long invoke(final CreateRelationCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());

        return relationRepository.save(new Relation(
                member,
                group,
                command.name(),
                command.imageUrl(),
                command.memo()
        )).getId();
    }
}
