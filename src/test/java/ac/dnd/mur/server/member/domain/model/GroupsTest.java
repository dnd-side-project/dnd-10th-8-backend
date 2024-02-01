package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_GROUP_ALREADY_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Member -> 도메인 [Groups] 테스트")
class GroupsTest extends UnitTest {
    private final Member member = MEMBER_1.toDomain().apply(1);

    @Test
    @DisplayName("사용자별로 기본적으로 [친구, 가족, 지인, 직장] Groups가 제공된다")
    void init() {
        // when
        final Groups groups = Groups.init(member);

        // then
        assertThat(groups.getGroups())
                .map(Group::getName)
                .containsExactlyInAnyOrder("친구", "가족", "지인", "직장");
    }

    @Nested
    @DisplayName("그룹 추가")
    class Add {
        @ParameterizedTest
        @ValueSource(strings = {"친구", "가족", "지인", "직장"})
        @DisplayName("이미 존재하면 중복으로 추가할 수 없다")
        void throwExceptionByAlreadyExists(final String name) {
            // given
            final Groups groups = Groups.init(member);

            // when - then
            assertThatThrownBy(() -> groups.add(member, name))
                    .isInstanceOf(MemberException.class)
                    .hasMessage(MEMBER_GROUP_ALREADY_EXISTS.getMessage());
        }

        @Test
        @DisplayName("그룹을 추가한다")
        void success() {
            // given
            final Groups groups = Groups.init(member);

            // when
            groups.add(member, "거래처");

            // then
            assertThat(groups.getGroups())
                    .map(Group::getName)
                    .containsExactlyInAnyOrder("친구", "가족", "지인", "직장", "거래처");
        }
    }
}
