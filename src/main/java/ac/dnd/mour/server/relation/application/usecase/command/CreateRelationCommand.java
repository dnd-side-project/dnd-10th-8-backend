package ac.dnd.mour.server.relation.application.usecase.command;

public record CreateRelationCommand(
        long memberId,
        long groupId,
        String name,
        String imageUrl,
        String memo
) {
}
