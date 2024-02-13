package ac.dnd.mour.server.relation.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.relation.application.usecase.command.UpdateRelationCommand;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateRelationUseCase {
    private final RelationRepository relationRepository;
    private final GroupRepository groupRepository;

    @MourWritableTransactional
    public void invoke(final UpdateRelationCommand command) {
        final Relation relation = relationRepository.getMemberRelation(command.relationId(), command.memberId());
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        relation.update(
                group,
                command.name(),
                command.imageUrl(),
                command.memo()
        );
    }
}
