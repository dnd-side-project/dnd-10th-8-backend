package ac.dnd.mur.server.heart.application.usecase.command;

public record DeleteHeartCommand(
        long memberId,
        long heartId
) {
}
