package ac.dnd.mur.server.member.domain.repository;

import ac.dnd.mur.server.member.domain.model.Group;
import ac.dnd.mur.server.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_GROUP_NOT_FOUND;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_GROUP_NOT_FOUND));
    }
}
