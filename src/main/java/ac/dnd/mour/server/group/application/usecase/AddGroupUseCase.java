package ac.dnd.mour.server.group.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.group.application.usecase.command.AddGroupCommand;
import ac.dnd.mour.server.group.domain.model.Group;
import ac.dnd.mour.server.group.domain.repository.GroupRepository;
import ac.dnd.mour.server.group.exception.GroupException;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mour.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;

@UseCase
@RequiredArgsConstructor
public class AddGroupUseCase {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @MourWritableTransactional
    public long invoke(final AddGroupCommand command) {
        final Member member = memberRepository.getById(command.memberId());
        validateExistsGroup(member, command.name());
        return groupRepository.save(Group.of(member, command.name())).getId();
    }

    private void validateExistsGroup(final Member member, final String name) {
        if (groupRepository.existsByMemberIdAndName(member.getId(), name)) {
            throw new GroupException(GROUP_ALREADY_EXISTS);
        }
    }
}
