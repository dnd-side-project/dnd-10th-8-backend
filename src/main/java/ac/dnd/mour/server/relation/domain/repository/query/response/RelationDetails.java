package ac.dnd.mour.server.relation.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

public record RelationDetails(
        long id,
        String name,
        String imageUrl,
        String memo,
        long groupId,
        String groupName
) {
    @QueryProjection
    public RelationDetails {
    }
}
