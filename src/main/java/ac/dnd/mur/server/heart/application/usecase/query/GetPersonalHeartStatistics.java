package ac.dnd.mur.server.heart.application.usecase.query;

public record GetPersonalHeartStatistics(
        String type,
        int year,
        int month
) {
}
