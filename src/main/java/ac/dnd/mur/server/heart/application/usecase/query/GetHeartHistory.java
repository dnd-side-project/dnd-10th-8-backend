package ac.dnd.mur.server.heart.application.usecase.query;

public record GetHeartHistory(
        long memberId,
        String sort,
        String name
) {
}
