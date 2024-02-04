package ac.dnd.mur.server.relation.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.domain.repository.HeartRepository;
import ac.dnd.mur.server.relation.application.usecase.query.GetMultipleRelationDetails;
import ac.dnd.mur.server.relation.application.usecase.query.GetSingleRelationDetails;
import ac.dnd.mur.server.relation.application.usecase.query.response.MultipleRelationDetails;
import ac.dnd.mur.server.relation.application.usecase.query.response.SingleRelationDetails;
import ac.dnd.mur.server.relation.domain.repository.query.RelationDetailsQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetRelationDetailsUseCase {
    private final RelationDetailsQueryRepository relationDetailsQueryRepository;
    private final HeartRepository heartRepository;

    @MurReadOnlyTransactional
    public SingleRelationDetails getRelation(final GetSingleRelationDetails query) {
        return SingleRelationDetails.of(
                relationDetailsQueryRepository.fetchRelation(query.relationId(), query.memberId()),
                heartRepository.fetchInteractionMoney(query.memberId(), query.relationId(), true),
                heartRepository.fetchInteractionMoney(query.memberId(), query.relationId(), false)
        );
    }

    @MurReadOnlyTransactional
    public List<MultipleRelationDetails> getRelations(final GetMultipleRelationDetails query) {
        return relationDetailsQueryRepository.fetchRelations(query.memberId(), query.name())
                .stream()
                .map(MultipleRelationDetails::from)
                .toList();
    }
}
