package ac.dnd.mour.server.heart.application.usecase.command;

public record DeleteHeartCommand(
        long memberId,
        long heartId
) {
}
