package ac.dnd.mur.server.heart.domain.repository;

import ac.dnd.mur.server.heart.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
