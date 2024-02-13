package ac.dnd.mour.server.heart.domain.repository;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.heart.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartTagRepository extends JpaRepository<Tag, Long> {
    @MourWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Tag t WHERE t.heart.id = :heartId")
    void deleteByHeartId(@Param("heartId") final Long heartId);

    @MourWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Tag t WHERE t.heart.id IN :heartIds")
    void deleteByHeartIds(@Param("heartIds") final List<Long> heartIds);
}
