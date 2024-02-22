package ac.dnd.mour.server.relation.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.domain.repository.HeartRepository;
import ac.dnd.mour.server.relation.application.usecase.query.GetMultipleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.GetSingleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.response.MultipleRelationDetails;
import ac.dnd.mour.server.relation.application.usecase.query.response.SingleRelationDetails;
import ac.dnd.mour.server.relation.domain.repository.query.RelationDetailsQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetRelationDetailsUseCase {
    private final RelationDetailsQueryRepository relationDetailsQueryRepository;
    private final HeartRepository heartRepository;

    @MourReadOnlyTransactional
    public SingleRelationDetails getRelation(final GetSingleRelationDetails query) {
        return SingleRelationDetails.of(
                relationDetailsQueryRepository.fetchRelation(query.relationId(), query.memberId()),
                heartRepository.fetchGivenMoney(query.memberId(), query.relationId()),
                heartRepository.fetchTakenMoney(query.memberId(), query.relationId())
        );
    }

    @MourReadOnlyTransactional
    public List<MultipleRelationDetails> getRelations(final GetMultipleRelationDetails query) {
        return relationDetailsQueryRepository.fetchRelations(query.memberId(), query.name())
                .stream()
                .map(MultipleRelationDetails::from)
                .toList();
    }
}
