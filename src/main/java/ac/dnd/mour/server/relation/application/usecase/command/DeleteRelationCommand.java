package ac.dnd.mour.server.relation.application.usecase.command;

public record DeleteRelationCommand(
        long memberId,
        long relationid
) {
}
