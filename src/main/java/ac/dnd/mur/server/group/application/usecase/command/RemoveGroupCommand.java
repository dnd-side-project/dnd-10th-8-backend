package ac.dnd.mur.server.group.application.usecase.command;

public record RemoveGroupCommand(
        long memberId,
        long groupId
) {
}
