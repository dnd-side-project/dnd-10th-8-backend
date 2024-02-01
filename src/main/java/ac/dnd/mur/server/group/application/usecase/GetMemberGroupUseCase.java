package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetMemberGroupUseCase {
    private final GroupRepository groupRepository;

    @MurReadOnlyTransactional
    public List<GroupResponse> invoke(final long memberId) {
        return groupRepository.findByMemberId(memberId)
                .stream()
                .map(GroupResponse::of)
                .toList();
    }
}
