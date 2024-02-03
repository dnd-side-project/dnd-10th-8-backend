package ac.dnd.mur.server.schedule.application.usecase.command;

public record DeleteScheduleCommand(
        long memberId,
        long scheduleId
) {
}
