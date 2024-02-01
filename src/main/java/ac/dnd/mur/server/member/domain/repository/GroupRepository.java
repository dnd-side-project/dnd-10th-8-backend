package ac.dnd.mur.server.member.domain.repository;

import ac.dnd.mur.server.member.domain.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
