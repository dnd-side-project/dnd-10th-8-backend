package ac.dnd.mur.server.relation.application.usecase.query.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;

public record SingleRelationDetails(
        long id,
        String name,
        String imageUrl,
        String memo,
        GroupResponse group,
        long giveMoney,
        long takeMoney
) {
    public static SingleRelationDetails of(
            final RelationDetails details,
            final long giveMoney,
            final long takeMoney
    ) {
        return new SingleRelationDetails(
                details.id(),
                details.name(),
                details.imageUrl(),
                details.memo(),
                new GroupResponse(details.groupId(), details.groupName()),
                giveMoney,
                takeMoney
        );
    }
}
