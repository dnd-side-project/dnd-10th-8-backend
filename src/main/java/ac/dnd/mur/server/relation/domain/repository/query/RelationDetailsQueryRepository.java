package ac.dnd.mur.server.relation.domain.repository.query;

import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;

public interface RelationDetailsQueryRepository {
    RelationDetails fetchRelation(final long id);
}
