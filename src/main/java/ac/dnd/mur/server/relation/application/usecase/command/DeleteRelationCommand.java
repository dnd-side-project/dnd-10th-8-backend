package ac.dnd.mur.server.relation.application.usecase.command;

public record DeleteRelationCommand(
        long relationid,
        long memberId
) {
}
