package ac.dnd.mur.server.group.domain.repository;

import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.group.exception.GroupException;
import org.springframework.data.jpa.repository.JpaRepository;

import static ac.dnd.mur.server.group.exception.GroupExceptionCode.GROUP_NOT_FOUND;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new GroupException(GROUP_NOT_FOUND));
    }
}
