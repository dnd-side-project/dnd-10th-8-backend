package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_2;
import static ac.dnd.bookkeeping.server.member.domain.model.Member.Status.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member -> 도메인 Aggregate [Member] 테스트")
class MemberTest extends UnitTest {
    @Test
    @DisplayName("카카오 계정의 경우 고객센터에서 이메일을 변경하는 경우 플랫폼에서 관리하는 이메일과 싱크를 맞춘다")
    void syncEmail() {
        // given
        final Member member = MEMBER_1.toDomain();

        // when
        member.syncEmail(MEMBER_2.getPlatform().getEmail());

        // then
        assertThat(member.getPlatform().getEmail().getValue()).isEqualTo(MEMBER_2.getPlatform().getEmail().getValue());
    }

    @Test
    @DisplayName("탈퇴 처리를 진행한다")
    void delete() {
        // given
        final Member member = MEMBER_1.toDomain().apply(1L);

        // when
        member.delete();

        // then
        assertAll(
                () -> assertThat(member.getId()).isEqualTo(1L),
                () -> assertThat(member.getPlatform()).isNull(),
                () -> assertThat(member.getProfileImageUrl()).isEqualTo(MEMBER_1.getProfileImageUrl()),
                () -> assertThat(member.getName()).isEqualTo(MEMBER_1.getName()),
                () -> assertThat(member.getNickname()).isNull(),
                () -> assertThat(member.getGender()).isEqualTo(MEMBER_1.getGender()),
                () -> assertThat(member.getBirth()).isEqualTo(MEMBER_1.getBirth()),
                () -> assertThat(member.getStatus()).isEqualTo(INACTIVE)
        );
    }
}
