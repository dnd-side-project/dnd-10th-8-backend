package ac.dnd.mur.server.statistics.application.usecase;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.global.annotation.UseCase;
import ac.dnd.mur.server.heart.domain.repository.query.HeartStatisticsRepository;
import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition;
import ac.dnd.mur.server.statistics.application.usecase.query.GetPersonalHeartStatistics;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mur.server.statistics.application.usecase.query.response.PersonalHeartSummary;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetPersonalHeartStatisticsUseCase {
    private static final List<String> totalEvents = List.of("결혼", "생일", "출산", "돌잔치", "개업", "기타");
    private static final List<String> specialEvents = List.of("결혼", "생일", "출산", "돌잔치", "개업");

    private final HeartStatisticsRepository heartStatisticsRepository;

    @MurReadOnlyTransactional
    public PersonalHeartStatisticsResponse invoke(final GetPersonalHeartStatistics query) {
        final List<PersonalHeartHistory> giveHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, true));
        final List<PersonalHeartHistory> takeHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, false));
        return new PersonalHeartStatisticsResponse(groupingByEvent(giveHistories), groupingByEvent(takeHistories));
    }

    private PersonalStatisticsCondition createGiveOrTakeCondition(final GetPersonalHeartStatistics query, final boolean give) {
        return new PersonalStatisticsCondition(
                query.memberId(),
                PersonalStatisticsCondition.Type.from(query.type()),
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
        return specialEvents.contains(event);
    }

    private void applyAllEvents(final List<Map<String, List<PersonalHeartSummary>>> result) {
        for (final String event : totalEvents) {
            final boolean eventExists = result.stream().anyMatch(map -> map.containsKey(event));
            if (!eventExists) {
                result.add(Map.of(event, List.of()));
            }
        }
    }
}
