package ac.dnd.mour.server.relation.application.usecase;

import ac.dnd.mour.server.global.annotation.MourWritableTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.relation.application.usecase.command.DeleteRelationCommand;
import ac.dnd.mour.server.relation.domain.model.Relation;
import ac.dnd.mour.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteRelationUseCase {
    private final RelationRepository relationRepository;

    @MourWritableTransactional
    public void invoke(final DeleteRelationCommand command) {
        final Relation relation = relationRepository.getMemberRelation(command.relationid(), command.memberId());
        relationRepository.deleteMemberRelation(relation.getId(), command.memberId());
    }
}
