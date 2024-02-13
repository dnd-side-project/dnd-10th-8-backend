package ac.dnd.mur.server.statistics.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.domain.repository.query.HeartStatisticsRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalHeartStatisticsCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.StatisticsStandard;
import ac.dnd.mur.server.statistics.application.usecase.query.GetPersonalHeartStatistics;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartSummary;
import ac.dnd.mur.server.statistics.domain.model.StatisticsCategory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetPersonalHeartStatisticsUseCase {
    private final HeartStatisticsRepository heartStatisticsRepository;

    @MurReadOnlyTransactional
    public PersonalHeartStatisticsResponse invoke(final GetPersonalHeartStatistics query) {
        final List<PersonalHeartHistory> giveHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, true));
        final List<PersonalHeartHistory> takeHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, false));
        return new PersonalHeartStatisticsResponse(groupingByEvent(giveHistories), groupingByEvent(takeHistories));
    }

    private PersonalHeartStatisticsCondition createGiveOrTakeCondition(final GetPersonalHeartStatistics query, final boolean give) {
        return new PersonalHeartStatisticsCondition(
                query.memberId(),
                StatisticsStandard.from(query.standard()),
                query.year(),
                query.month(),
                give
        );
    }

    private List<Map<String, List<PersonalHeartSummary>>> groupingByEvent(final List<PersonalHeartHistory> histories) {
        final List<Map<String, List<PersonalHeartSummary>>> result = histories.stream()
                .collect(Collectors.groupingBy(
                        it -> isSpecialEvent(it.event()) ? it.event() : "기타",
                        Collectors.mapping(
                                it -> new PersonalHeartSummary(
                                        it.relationName(),
                                        it.groupName(),
                                        it.money(),
                                        it.day(),
                                        it.memo()
                                ),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        applyAllEvents(result);
        return result;
    }

    private boolean isSpecialEvent(final String event) {
        return StatisticsCategory.isSpecialEvent(event);
    }

    private void applyAllEvents(final List<Map<String, List<PersonalHeartSummary>>> result) {
        final List<String> totalEvents = StatisticsCategory.getTotalEvents();
        for (final String event : totalEvents) {
            final boolean eventExists = result.stream().anyMatch(map -> map.containsKey(event));
            if (!eventExists) {
                result.add(Map.of(event, List.of()));
            }
        }
    }
}