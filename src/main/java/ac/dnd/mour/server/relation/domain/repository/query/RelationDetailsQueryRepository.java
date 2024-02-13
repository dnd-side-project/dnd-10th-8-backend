package ac.dnd.mour.server.relation.domain.repository.query;

import ac.dnd.mour.server.relation.domain.repository.query.response.RelationDetails;
import ac.dnd.mour.server.relation.domain.repository.query.response.RelationSummary;

import java.util.List;

public interface RelationDetailsQueryRepository {
    RelationDetails fetchRelation(final long id, final long memberId);

    List<RelationSummary> fetchRelations(final long memberId, final String name);
}
