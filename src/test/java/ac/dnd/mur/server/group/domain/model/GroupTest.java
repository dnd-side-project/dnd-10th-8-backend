package ac.dnd.mur.server.group.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_GROUP_NAME_TOO_LONG;
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
}
