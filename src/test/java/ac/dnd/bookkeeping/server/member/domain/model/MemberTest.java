package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_2;
import static org.assertj.core.api.Assertions.assertThat;

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
}
