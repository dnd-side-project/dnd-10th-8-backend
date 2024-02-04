package ac.dnd.mur.server.heart.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.exception.HeartException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static ac.dnd.mur.server.heart.exception.HeartExceptionCode.HEART_NOT_FOUND;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    default Heart getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new HeartException(HEART_NOT_FOUND));
    }

    // @Query
    @Query("""
            SELECT h.id
            FROM Heart h
            WHERE h.memberId = :memberId
            """)
    List<Long> findIdsByMemberId(@Param("memberId") final Long memberId);

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Heart h WHERE h.id = :id AND h.memberId = :memberId")
    void deleteMemberHeart(@Param("id") final long id, @Param("memberId") final long memberId);

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Heart h WHERE h.memberId = :memberId")
    void deleteMemberHearts(@Param("memberId") final Long memberId);

    @Query("""
            SELECT SUM(h.money)
            FROM Heart h
            WHERE h.memberId = :memberId AND h.relationId = :relationId AND h.give = :give
            """)
    long fetchInteractionMoney(
            @Param("memberId") final long memberId,
            @Param("relationId") final long relationId,
            @Param("give") final boolean give
    );

    // Query Method
    Optional<Heart> findByIdAndMemberId(final long id, final long memberId);

    default Heart getMemberHeart(final long id, final long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new HeartException(HEART_NOT_FOUND));
    }
}
