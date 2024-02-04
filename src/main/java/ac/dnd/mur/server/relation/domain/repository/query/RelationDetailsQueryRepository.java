package ac.dnd.mur.server.relation.domain.repository.query;

import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;

import java.util.List;

public interface RelationDetailsQueryRepository {
    RelationDetails fetchRelation(final long id, final long memberId);

    List<RelationDetails> fetchRelations(final long memberId, final String name);
}
