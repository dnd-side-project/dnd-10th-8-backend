package ac.dnd.mur.server.relation.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.relation.application.usecase.command.UpdateRelationCommand;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateRelationUseCase {
    private final RelationRepository relationRepository;
    private final GroupRepository groupRepository;

    @MurWritableTransactional
    public void invoke(final UpdateRelationCommand command) {
        final Relation relation = relationRepository.getById(command.relationId());
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        relation.update(
                group,
                command.name(),
                command.phone(),
                command.memo()
        );
    }
}
