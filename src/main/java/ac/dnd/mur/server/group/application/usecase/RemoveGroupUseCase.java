package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.application.usecase.command.RemoveGroupCommand;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RemoveGroupUseCase {
    private final GroupRepository groupRepository;

    @MurWritableTransactional
    public void invoke(final RemoveGroupCommand command) {
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        groupRepository.deleteGroup(group.getId());
    }
}
