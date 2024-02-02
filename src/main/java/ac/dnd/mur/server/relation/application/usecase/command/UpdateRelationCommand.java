package ac.dnd.mur.server.relation.application.usecase.command;

public record UpdateRelationCommand(
        long relationId,
        long memberId,
        long groupId,
        String name,
        String phone,
        String memo
) {
}
