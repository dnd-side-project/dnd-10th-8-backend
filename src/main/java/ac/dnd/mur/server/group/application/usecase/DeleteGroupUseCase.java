package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteGroupUseCase {
    private final GroupRepository groupRepository;

    public void invoke(final long groupId) {
        groupRepository.deleteGroup(groupId);
    }
}
