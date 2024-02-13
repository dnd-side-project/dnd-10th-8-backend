package ac.dnd.mour.server.relation.application.usecase.command;

public record UpdateRelationCommand(
        long memberId,
        long relationId,
        long groupId,
        String name,
        String imageUrl,
        String memo
) {
}
