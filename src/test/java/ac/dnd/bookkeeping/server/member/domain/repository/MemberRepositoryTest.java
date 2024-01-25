package ac.dnd.bookkeeping.server.member.domain.repository;

import ac.dnd.bookkeeping.server.common.RepositoryTest;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.bookkeeping.server.common.fixture.MemberFixture.MEMBER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member -> MemberRepository 테스트")
class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository sut;

    @Test
    @DisplayName("소셜 플랫폼 ID를 기반으로 사용자를 조회한다")
    void findByPlatformSocialId() {
        // given
        final Member member1 = sut.save(MEMBER_1.toDomain());
        final Member member2 = sut.save(MEMBER_2.toDomain());

        // when
        final Optional<Member> findMember1 = sut.findByPlatformSocialId(member1.getPlatform().getSocialId());
        final Optional<Member> findMember2 = sut.findByPlatformSocialId(member1.getPlatform().getSocialId() + "diff");
        final Optional<Member> findMember3 = sut.findByPlatformSocialId(member2.getPlatform().getSocialId());
        final Optional<Member> findMember4 = sut.findByPlatformSocialId(member2.getPlatform().getSocialId() + "diff");

        // then
        assertAll(
                () -> assertThat(findMember1).isPresent(),
                () -> assertThat(findMember2).isEmpty(),
                () -> assertThat(findMember3).isPresent(),
                () -> assertThat(findMember4).isEmpty()
        );
    }

    @Test
    @DisplayName("닉네임이 사용중인지 확인한다")
    void existsByNickname() {
        // given
        sut.save(MEMBER_1.toDomain());

        // when
        final boolean actual1 = sut.existsByNickname(MEMBER_1.getNickname());
        final boolean actual2 = sut.existsByNickname(MEMBER_2.getNickname());

        // then
        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse()
        );
    }
}
