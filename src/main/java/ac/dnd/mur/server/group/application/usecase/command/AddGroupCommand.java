package ac.dnd.mur.server.group.application.usecase.command;

public record AddGroupCommand(
        long memberId,
        String name
) {
}
