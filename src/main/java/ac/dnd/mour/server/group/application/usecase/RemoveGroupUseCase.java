package ac.dnd.mour.server.group.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.application.usecase.command.RemoveGroupCommand;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.group.exception.GroupException;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.group.exception.GroupExceptionCode.CANNOT_DELETE_FROM_REGISTERED_RELATIONSHIP_EXISTS;

@UseCase
@RequiredArgsConstructor
public class RemoveGroupUseCase {
    private final RelationRepository relationRepository;
    private final GroupRepository groupRepository;

    @MourWritableTransactional
    public void invoke(final RemoveGroupCommand command) {
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        validateRegisteredRelationshipWithGroupExists(command.memberId(), group.getId());
        groupRepository.deleteGroup(group.getId());
    }

    private void validateRegisteredRelationshipWithGroupExists(final long memberId, final long groupId) {
        if (relationRepository.existsByMemberIdAndGroupId(memberId, groupId)) {
            throw new GroupException(CANNOT_DELETE_FROM_REGISTERED_RELATIONSHIP_EXISTS);
        }
    }
}
