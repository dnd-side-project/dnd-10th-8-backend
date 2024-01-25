package ac.dnd.bookkeeping.server.member.domain.repository;

import ac.dnd.bookkeeping.server.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
