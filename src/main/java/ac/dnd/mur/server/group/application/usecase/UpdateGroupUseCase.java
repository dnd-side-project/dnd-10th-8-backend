package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.application.usecase.command.UpdateGroupCommand;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.group.exception.GroupException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;

@UseCase
@RequiredArgsConstructor
public class UpdateGroupUseCase {
    private final GroupRepository groupRepository;

    @MurWritableTransactional
    public void invoke(final UpdateGroupCommand command) {
        final Group group = groupRepository.getMemberGroup(command.groupId(), command.memberId());
        validateExistsGroup(group, command.memberId(), command.name());
        group.update(command.name());
    }

    private void validateExistsGroup(final Group group, final long memberId, final String name) {
        if (!group.getName().equals(name) && groupRepository.existsByMemberIdAndName(memberId, name)) {
            throw new GroupException(GROUP_ALREADY_EXISTS);
        }
    }
}
