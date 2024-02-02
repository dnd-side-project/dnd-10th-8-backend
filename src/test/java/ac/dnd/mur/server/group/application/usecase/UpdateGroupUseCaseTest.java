package ac.dnd.mur.server.group.application.usecase;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.group.application.usecase.command.UpdateGroupCommand;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.domain.repository.GroupRepository;
import ac.dnd.mur.server.group.exception.GroupException;
import ac.dnd.mur.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.mur.server.common.fixture.GroupFixture.친구;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_ALREADY_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Group -> UpdateGroupUseCase 테스트")
class UpdateGroupUseCaseTest extends UnitTest {
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final UpdateGroupUseCase sut = new UpdateGroupUseCase(groupRepository);

    private final Member member = MEMBER_1.toDomain().apply(1L);
    private final Group group = 친구.toDomain(member).apply(1L);

    @Test
    @DisplayName("현재 그룹명과 동일하지 않고 이미 관리하고 있는 그룹명이면 수정에 실패한다")
    void throwExceptionByGroupAlreadyExists() {
        // given
        final UpdateGroupCommand command = new UpdateGroupCommand(
                member.getId(),
                group.getId(),
                "거래처"
        );
        given(groupRepository.getMemberGroup(command.groupId(), command.memberId())).willReturn(group);
        given(groupRepository.existsByMemberIdAndName(command.memberId(), command.name())).willReturn(true);

        // when - then
        assertThatThrownBy(() -> sut.invoke(command))
                .isInstanceOf(GroupException.class)
                .hasMessage(GROUP_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("현재 그룹명과 동일한 이름으로 수정을 시도하면 유지한채로 성공한다")
    void successWithKeep() {
        // given
        final UpdateGroupCommand command = new UpdateGroupCommand(
                member.getId(),
                group.getId(),
                "친구"
        );
        given(groupRepository.getMemberGroup(command.groupId(), command.memberId())).willReturn(group);
        given(groupRepository.existsByMemberIdAndName(command.memberId(), command.name())).willReturn(true);

        // when
        sut.invoke(command);

        // then
        assertThat(group.getName()).isEqualTo(command.name());
    }

    @Test
    @DisplayName("현재 그룹명과 다른 이름으로 수정을 시도하면 성공한다")
    void success() {
        // given
        final UpdateGroupCommand command = new UpdateGroupCommand(
                member.getId(),
                group.getId(),
                "거래처"
        );
        given(groupRepository.getMemberGroup(command.groupId(), command.memberId())).willReturn(group);
        given(groupRepository.existsByMemberIdAndName(command.memberId(), command.name())).willReturn(false);

        // when
        sut.invoke(command);

        // then
        assertThat(group.getName()).isEqualTo(command.name());
    }
}
