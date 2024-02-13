package ac.dnd.mour.server.relation.domain.model.response;

import ac.dnd.mour.server.group.domain.model.GroupResponse;

public record RelationSummary(
        long id,
        String name,
        GroupResponse group
) {
}
