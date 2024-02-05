package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition;

import java.util.List;

public interface HeartStatisticsRepository {
    List<PersonalHeartHistory> fetchPersonalHeartHistories(final PersonalStatisticsCondition condition);
}
