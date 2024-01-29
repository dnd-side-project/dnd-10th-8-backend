package ac.dnd.mur.server.member.domain.repository;

import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.member.domain.model.Nickname;
import ac.dnd.mur.server.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

public interface MemberRepository extends JpaRepository<Member, Long> {
    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    Optional<Member> findByPlatformSocialId(final String socialId);

    boolean existsByNickname(final Nickname nickname);
}
