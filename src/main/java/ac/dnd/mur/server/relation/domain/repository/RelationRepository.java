package ac.dnd.mur.server.relation.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.exception.RelationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static ac.dnd.mur.server.relation.exception.RelationExceptionCode.RELATION_NOT_FOUND;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    default Relation getById(final long id) {
        return findById(id)
                .orElseThrow(() -> new RelationException(RELATION_NOT_FOUND));
    }

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Relation r WHERE r.id = :id AND r.memberId = :memberId")
    void deleteMemberRelation(@Param("id") final long id, @Param("memberId") final long memberId);

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Relation r WHERE r.memberId = :memberId")
    void deleteMemberRelations(@Param("memberId") final long memberId);

    Optional<Relation> findByIdAndMemberId(final long id, final long memberId);

    default Relation getMemberRelation(final long id, final long memberId) {
        return findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new RelationException(RELATION_NOT_FOUND));
    }
}
