package ac.dnd.mour.server.relation.application.usecase.query.response;

import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.relation.domain.repository.query.response.RelationSummary;

public record MultipleRelationDetails(
        long id,
        String name,
        GroupResponse group
) {
    public static MultipleRelationDetails from(final RelationSummary details) {
        return new MultipleRelationDetails(
                details.id(),
                details.name(),
                new GroupResponse(details.groupId(), details.groupName())
        );
    }
}
