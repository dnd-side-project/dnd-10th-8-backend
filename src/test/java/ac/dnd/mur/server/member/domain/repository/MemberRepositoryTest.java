package ac.dnd.mur.server.member.domain.repository;

import ac.dnd.mur.server.common.RepositoryTest;
import ac.dnd.mur.server.member.domain.model.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_1;
import static ac.dnd.mur.server.common.fixture.MemberFixture.MEMBER_2;
import static ac.dnd.mur.server.member.domain.model.Member.Status.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member -> MemberRepository 테스트")
class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository sut;

    @PersistenceContext
    private EntityManager em;

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

    @Test
    @DisplayName("사용자를 삭제한다 (Soft Delete)")
    void delete() {
        // given
        final Member member = sut.save(MEMBER_1.toDomain());

        // when
        sut.delete(member.getId());

        // then
        assertAll(
                () -> assertThatThrownBy(() -> getByJpql(member.getId())).isInstanceOf(NoResultException.class),
                () -> {
                    final Member findMember = getByNative(member.getId());

                    // Affected
                    assertThat(findMember.getPlatform()).isNull();
                    assertThat(findMember.getNickname()).isNull();
                    assertThat(findMember.getStatus()).isEqualTo(INACTIVE);

                    // Maintain
                    assertThat(findMember.getProfileImageUrl()).isEqualTo(MEMBER_1.getProfileImageUrl());
                    assertThat(findMember.getName()).isEqualTo(MEMBER_1.getName());
                    assertThat(findMember.getGender()).isEqualTo(MEMBER_1.getGender());
                    assertThat(findMember.getBirth()).isEqualTo(MEMBER_1.getBirth());
                }
        );
    }

    private Member getByJpql(final long id) {
        return em.createQuery("SELECT m FROM Member m WHERE m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    private Member getByNative(final long id) {
        return (Member) em.createNativeQuery("SELECT * FROM member m WHERE m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
