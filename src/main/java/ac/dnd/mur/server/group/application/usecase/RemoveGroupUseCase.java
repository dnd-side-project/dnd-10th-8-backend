package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.application.usecase.command.RemoveGroupCommand;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RemoveGroupUseCase {
    private final GroupRepository groupRepository;

    public void invoke(final RemoveGroupCommand command) {
        groupRepository.deleteGroup(command.groupId(), command.memberId());
    }
}
