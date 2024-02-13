package ac.dnd.mour.server.schedule.application.usecase.command;

public record DeleteScheduleCommand(
        long memberId,
        long scheduleId
) {
}
