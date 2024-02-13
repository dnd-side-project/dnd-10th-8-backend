package ac.dnd.mour.server.group.domain.model;

import ac.dnd.mour.server.common.UnitTest;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ac.dnd.mour.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mour.server.member.exception.MemberExceptionCode.MEMBER_GROUP_NAME_TOO_LONG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Group -> 도메인 [Group] 테스트")
class GroupTest extends UnitTest {
    private final Member member = MEMBER_1.toDomain().apply(1L);

    @Test
    @DisplayName("사용자 그룹 이름이 너무 길면 생성할 수 없다 (8자)")
    void throwExceptionByNameTooLong() {
        assertThatThrownBy(() -> Group.of(member, "a".repeat(9)))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_GROUP_NAME_TOO_LONG.getMessage());
    }

    @Test
    @DisplayName("사용자 그룹을 생성한다")
    void success() {
        assertDoesNotThrow(() -> Group.of(member, "test"));
    }

    @Test
    @DisplayName("사용자에게 기본적으로 제공되는 그룹 리스트를 생성한다")
    void init() {
        // when
        final List<Group> groups = Group.init(member);

        // then
        assertThat(groups)
                .map(Group::getName)
                .containsExactlyInAnyOrder("친구", "가족", "지인", "직장");
    }
}
