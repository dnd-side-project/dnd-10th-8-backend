package ac.dnd.mour.server.relation.application.usecase.query;

public record GetSingleRelationDetails(
        long memberId,
        long relationId
) {
}
