package ac.dnd.mur.server.relation.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

public record RelationDetails(
        long id,
        String name,
        long groupId,
        String groupName
) {
    @QueryProjection
    public RelationDetails {
    }
}
