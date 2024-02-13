package ac.dnd.mour.server.relation.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

public record RelationSummary(
        long id,
        String name,
        long groupId,
        String groupName
) {
    @QueryProjection
    public RelationSummary {
    }
}
