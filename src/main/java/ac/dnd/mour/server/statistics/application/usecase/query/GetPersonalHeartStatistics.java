package ac.dnd.mour.server.statistics.application.usecase.query;

public record GetPersonalHeartStatistics(
        long memberId,
        int year,
        int month
) {
}
