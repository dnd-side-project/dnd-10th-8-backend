package ac.dnd.mour.server.heart.application.usecase.query;

public record GetHeartHistory(
        long memberId,
        String sort,
        String name
) {
}
