package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;

import java.util.List;

public interface HeartSearchRepository {
    List<HeartHistory> fetchHeartsByCondition(final SearchHeartCondition condition);
}
