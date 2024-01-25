package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_2;
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
    @DisplayName("온보딩 과정에서 추가 정보들을 기입한다 (닉네임, 성별, 생년월일)")
    void complete() {
        // given
        final Member member = Member.create(MEMBER_1.getPlatform(), MEMBER_1.getProfileImageUrl());
        assertAll(
                () -> assertThat(member.getPlatform().getSocialId()).isEqualTo(MEMBER_1.getPlatform().getSocialId()),
                () -> assertThat(member.getPlatform().getEmail().getValue()).isEqualTo(MEMBER_1.getPlatform().getEmail().getValue()),
                () -> assertThat(member.getProfileImageUrl()).isEqualTo(MEMBER_1.getProfileImageUrl()),
                () -> assertThat(member.getNickname()).isNull(),
                () -> assertThat(member.getGender()).isNull(),
                () -> assertThat(member.getBirth()).isNull()
        );

        // when
        member.complete(MEMBER_2.getNickname(), MEMBER_1.getGender(), MEMBER_1.getBirth());

        // then
        assertAll(
                () -> assertThat(member.getPlatform().getSocialId()).isEqualTo(MEMBER_1.getPlatform().getSocialId()),
                () -> assertThat(member.getPlatform().getEmail().getValue()).isEqualTo(MEMBER_1.getPlatform().getEmail().getValue()),
                () -> assertThat(member.getProfileImageUrl()).isEqualTo(MEMBER_1.getProfileImageUrl()),
                () -> assertThat(member.getNickname().getValue()).isEqualTo(MEMBER_2.getNickname().getValue()),
                () -> assertThat(member.getGender()).isEqualTo(MEMBER_1.getGender()),
                () -> assertThat(member.getBirth()).isEqualTo(MEMBER_1.getBirth())
        );
    }
}
