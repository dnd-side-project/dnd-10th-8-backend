package ac.dnd.mur.server.relation.domain.repository;

import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.exception.RelationException;
import org.springframework.data.jpa.repository.JpaRepository;

import static ac.dnd.mur.server.relation.exception.RelationExceptionCode.RELATION_NOT_FOUND;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    default Relation getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new RelationException(RELATION_NOT_FOUND));
    }
}
