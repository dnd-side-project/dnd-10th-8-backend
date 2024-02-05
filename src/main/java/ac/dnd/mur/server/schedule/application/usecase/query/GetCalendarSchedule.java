package ac.dnd.mur.server.schedule.application.usecase.query;

public record GetCalendarSchedule(
        long memberId,
        int year,
        int month
) {
}
