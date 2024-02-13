package ac.dnd.mour.server.heart.domain.repository.query;

import ac.dnd.mour.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mour.server.heart.domain.repository.query.response.TrendHeartStatistics;
import ac.dnd.mour.server.heart.domain.repository.query.spec.PersonalHeartStatisticsCondition;
import ac.dnd.mour.server.heart.domain.repository.query.spec.TrendHeartStatisticsCondition;

import java.util.List;

public interface HeartStatisticsRepository {
    List<PersonalHeartHistory> fetchPersonalHeartHistories(final PersonalHeartStatisticsCondition condition);

    List<TrendHeartStatistics> fetchTrendHeartAveragePerEvent(final TrendHeartStatisticsCondition condition);
}
