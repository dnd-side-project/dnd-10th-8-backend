package ac.dnd.mur.server.heart.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.application.usecase.query.GetHeartHistory;
import ac.dnd.mur.server.heart.application.usecase.query.GetHeartHistoryWithRelation;
import ac.dnd.mur.server.heart.application.usecase.query.response.HeartHistoryDetails;
import ac.dnd.mur.server.heart.application.usecase.query.response.SpecificRelationHeartHistoryDetails;
import ac.dnd.mur.server.heart.domain.repository.query.HeartSearchRepository;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchSpecificRelationHeartCondition;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetHeartHistoryUseCase {
    private final HeartSearchRepository heartSearchRepository;

    @MurReadOnlyTransactional
    public List<HeartHistoryDetails> getHeartHistories(final GetHeartHistory query) {
        final SearchHeartCondition condition = new SearchHeartCondition(
                query.memberId(),
                SearchHeartCondition.Sort.from(query.sort()),
                query.name()
        );
        return heartSearchRepository.fetchHeartsByCondition(condition)
                .stream()
                .map(HeartHistoryDetails::from)
                .toList();
    }

    @MurReadOnlyTransactional
    public List<SpecificRelationHeartHistoryDetails> getHeartHistoriesWithRelation(final GetHeartHistoryWithRelation query) {
        final SearchSpecificRelationHeartCondition condition = new SearchSpecificRelationHeartCondition(
                query.memberId(),
                query.relationId(),
                SearchSpecificRelationHeartCondition.Sort.from(query.sort())
        );
        return heartSearchRepository.fetchHeartsWithSpecificRelation(condition)
                .stream()
                .map(SpecificRelationHeartHistoryDetails::from)
                .toList();
    }
}
