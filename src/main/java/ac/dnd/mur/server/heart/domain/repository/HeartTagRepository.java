package ac.dnd.mur.server.heart.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.heart.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartTagRepository extends JpaRepository<Tag, Long> {
    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Tag t WHERE t.heart.id = :heartId")
    void deleteByHeartId(@Param("heartId") final Long heartId);

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Tag t WHERE t.heart.id IN :heartIds")
    void deleteByHeartIds(@Param("heartIds") final List<Long> heartIds);
}
