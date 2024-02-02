package ac.dnd.mur.server.relation.application.usecase.command;

public record CreateRelationCommand(
        long memberId,
        long groupId,
        String name,
        String phone,
        String memo
) {
}
