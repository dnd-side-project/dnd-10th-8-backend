package ac.dnd.mur.server.group.application.usecase.command;

public record UpdateGroupCommand(
        long memberId,
        long groupId,
        String name
) {
}
