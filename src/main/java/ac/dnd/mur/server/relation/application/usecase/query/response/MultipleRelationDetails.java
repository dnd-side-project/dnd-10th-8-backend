package ac.dnd.mur.server.relation.application.usecase.query.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;

public record MultipleRelationDetails(
        long id,
        String name,
        GroupResponse group
) {
    public static MultipleRelationDetails from(final RelationDetails details) {
        return new MultipleRelationDetails(
                details.id(),
                details.name(),
                new GroupResponse(details.groupId(), details.groupName())
        );
    }
}