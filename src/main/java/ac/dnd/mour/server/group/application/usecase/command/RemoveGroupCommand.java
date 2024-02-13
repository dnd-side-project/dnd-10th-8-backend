package ac.dnd.mour.server.group.application.usecase.command;

public record RemoveGroupCommand(
        long memberId,
        long groupId
) {
}
