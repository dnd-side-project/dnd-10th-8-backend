package ac.dnd.mur.server.relation.application.usecase;

import ac.dnd.mur.server.global.annotation.MurWritableTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.relation.application.usecase.command.DeleteRelationCommand;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.relation.domain.repository.RelationRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteRelationUseCase {
    private final RelationRepository relationRepository;

    @MurWritableTransactional
    public void invoke(final DeleteRelationCommand command) {
        final Relation relation = relationRepository.getMemberRelation(command.relationid(), command.memberId());
        relationRepository.deleteMemberRelation(relation.getId(), command.memberId());
    }
}
