package ac.dnd.mur.server.relation.domain.repository;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.exception.RelationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static ac.dnd.mur.server.relation.exception.RelationExceptionCode.RELATION_NOT_FOUND;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    default Relation getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new RelationException(RELATION_NOT_FOUND));
    }

    @MurWritableTransactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Relation r WHERE r.memberId = :memberId")
    void deleteMemberRelations(@Param("memberId") final Long memberId);
}
