package ac.dnd.mour.server.member.domain.repository;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.domain.model.Nickname;
import ac.dnd.mour.server.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static ac.dnd.mour.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

public interface MemberRepository extends JpaRepository<Member, Long> {
    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    Optional<Member> findByPlatformSocialId(final String socialId);

    boolean existsByNickname(final Nickname nickname);

    // TODO 추후 Soft Delete or Hard Delete 판단 (일단 현재는 Soft Delete)
    @MourWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE Member m
            SET m.status = 'INACTIVE',
                m.platform = null,
                m.nickname = null
            WHERE m.id = :memberId
            """)
    void delete(@Param("memberId") final Long memberId);
}
