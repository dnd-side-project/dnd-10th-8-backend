package ac.dnd.mour.server.schedule.application.usecase.query;

public record GetScheduleDetails(
        long memberId,
        long scheduleId
) {
}
