package ac.dnd.mour.server.schedule.application.usecase.command;

public record HideScheduleCommand(
        long memberId,
        long scheduleId
) {
}
