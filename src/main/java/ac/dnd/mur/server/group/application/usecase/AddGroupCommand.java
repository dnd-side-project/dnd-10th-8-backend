package ac.dnd.mur.server.group.application.usecase;

public record AddGroupCommand(
        long memberId,
        String name
) {
}
