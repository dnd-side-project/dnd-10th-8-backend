package ac.dnd.mur.server.relation.domain.repository;

import ac.dnd.mur.server.relation.domain.model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation, Long> {
}
