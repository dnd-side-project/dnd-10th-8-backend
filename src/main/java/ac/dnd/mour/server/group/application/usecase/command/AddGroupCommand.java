package ac.dnd.mour.server.group.application.usecase.command;

public record AddGroupCommand(
        long memberId,
        String name
) {
}
