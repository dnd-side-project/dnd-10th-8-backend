package ac.dnd.mour.server.statistics.application.usecase.query;

public record GetPersonalHeartStatistics(
        long memberId,
        String standard,
        int year,
        int month
) {
}
