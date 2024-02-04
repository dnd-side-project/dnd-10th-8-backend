package ac.dnd.mur.server.relation.application.usecase.query;

public record GetSingleRelationDetails(
        long memberId,
        long relationId
) {
}
