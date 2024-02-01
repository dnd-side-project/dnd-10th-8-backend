package ac.dnd.mur.server.group.domain.repository;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Group -> GroupRepository 테스트")
class GroupRepositoryTest extends RepositoryTest {
    @Autowired
    private GroupRepository sut;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자가 관리하고 있는 그룹인지 확인한다")
    void existsByMemberIdAndName() {
        // given
        final Member member = memberRepository.save(MEMBER_1.toDomain());
        sut.saveAll(Group.init(member));

        // when
        final boolean actual1 = sut.existsByMemberIdAndName(member.getId(), "친구");
        final boolean actual2 = sut.existsByMemberIdAndName(member.getId(), "가족");
        final boolean actual3 = sut.existsByMemberIdAndName(member.getId(), "지인");
        final boolean actual4 = sut.existsByMemberIdAndName(member.getId(), "직장");
        final boolean actual5 = sut.existsByMemberIdAndName(member.getId(), "거래처");

        // then
        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isTrue(),
                () -> assertThat(actual3).isTrue(),
                () -> assertThat(actual4).isTrue(),
                () -> assertThat(actual5).isFalse()
        );
    }

    @Test
    @DisplayName("사용자가 관리하고 있는 그룹을 조회한다")
    void findByMemberId() {
        // given
        final Member member = memberRepository.save(MEMBER_1.toDomain());
        sut.saveAll(Group.init(member));

        // when
        final List<Group> result = sut.findByMemberId(member.getId());

        // then
        assertThat(result)
                .map(Group::getName)
                .containsExactlyInAnyOrder("친구", "가족", "지인", "직장");
    }
}
