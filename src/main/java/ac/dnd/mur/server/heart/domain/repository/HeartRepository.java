package ac.dnd.mur.server.heart.domain.repository;

import ac.dnd.mur.server.heart.domain.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
}
