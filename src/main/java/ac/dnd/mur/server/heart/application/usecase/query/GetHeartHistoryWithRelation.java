package ac.dnd.mur.server.heart.application.usecase.query;

public record GetHeartHistoryWithRelation(
        long memberId,
        long relationId,
        String sort
) {
}
