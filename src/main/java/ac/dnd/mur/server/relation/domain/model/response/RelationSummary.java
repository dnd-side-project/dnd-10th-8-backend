package ac.dnd.mur.server.relation.domain.model.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;

public record RelationSummary(
        long id,
        String name,
        GroupResponse group
) {
}
