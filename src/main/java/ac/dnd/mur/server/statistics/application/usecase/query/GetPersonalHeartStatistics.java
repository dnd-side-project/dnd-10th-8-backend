package ac.dnd.mur.server.statistics.application.usecase.query;

public record GetPersonalHeartStatistics(
        long memberId,
        String type,
        int year,
        int month
) {
}
