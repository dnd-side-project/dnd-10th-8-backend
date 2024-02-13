package ac.dnd.mour.server.group.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.application.usecase.command.RemoveGroupCommand;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RemoveGroupUseCase {
    private final GroupRepository groupRepository;

    @MourWritableTransactional
    public void invoke(final RemoveGroupCommand command) {
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        groupRepository.deleteGroup(group.getId());
    }
}
