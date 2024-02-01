package ac.dnd.mur.server.heart.domain.repository;

import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.exception.HeartException;
import org.springframework.data.jpa.repository.JpaRepository;

import static ac.dnd.mur.server.heart.exception.HeartExceptionCode.HEART_NOT_FOUND;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    default Heart getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new HeartException(HEART_NOT_FOUND));
    }
}
