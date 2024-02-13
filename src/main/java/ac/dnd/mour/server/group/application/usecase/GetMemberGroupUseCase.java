package ac.dnd.mour.server.group.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetMemberGroupUseCase {
    private final GroupRepository groupRepository;

    @MourReadOnlyTransactional
    public List<GroupResponse> invoke(final long memberId) {
        return groupRepository.findByMemberId(memberId)
                .stream()
                .map(GroupResponse::of)
                .toList();
    }
}
