package ac.dnd.mour.server.group.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.application.usecase.command.UpdateGroupCommand;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.group.exception.GroupException;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;

@UseCase
@RequiredArgsConstructor
public class UpdateGroupUseCase {
    private final GroupRepository groupRepository;

    @MourWritableTransactional
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
