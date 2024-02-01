package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.group.application.usecase.command.AddGroupCommand;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.group.exception.GroupException;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;

@UseCase
@RequiredArgsConstructor
public class AddGroupUseCase {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

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
