package ac.dnd.mour.server.heart.domain.repository.query;

import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mour.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mour.server.heart.domain.repository.query.spec.SearchSpecificRelationHeartCondition;

import java.util.List;

public interface HeartSearchRepository {
    List<Heart> fetchHeartsWithSpecificRelation(final SearchSpecificRelationHeartCondition condition);

    List<HeartHistory> fetchHeartsByCondition(final SearchHeartCondition condition);
}
