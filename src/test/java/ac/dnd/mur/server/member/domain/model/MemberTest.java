package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_2;
import static ac.dnd.mur.server.member.domain.model.Member.Status.ACTIVE;
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
    @DisplayName("정보를 수정한다")
    void update() {
        // given
        final Member member = MEMBER_1.toDomain();

        // when
        member.update(
                MEMBER_2.getProfileImageUrl(),
                MEMBER_2.getNickname(),
                MEMBER_2.getGender(),
                MEMBER_2.getBirth()
        );

        // then
        assertAll(
                () -> assertThat(member.getPlatform().getType()).isEqualTo(MEMBER_1.getPlatform().getType()),
                () -> assertThat(member.getPlatform().getSocialId()).isEqualTo(MEMBER_1.getPlatform().getSocialId()),
                () -> assertThat(member.getPlatform().getEmail().getValue()).isEqualTo(MEMBER_1.getPlatform().getEmail().getValue()),
                () -> assertThat(member.getProfileImageUrl()).isEqualTo(MEMBER_2.getProfileImageUrl()),
                () -> assertThat(member.getName()).isEqualTo(MEMBER_1.getName()),
                () -> assertThat(member.getNickname().getValue()).isEqualTo(MEMBER_2.getNickname().getValue()),
                () -> assertThat(member.getGender()).isEqualTo(MEMBER_2.getGender()),
                () -> assertThat(member.getBirth()).isEqualTo(MEMBER_2.getBirth()),
                () -> assertThat(member.getStatus()).isEqualTo(ACTIVE)
        );
    }
}
