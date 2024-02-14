package ac.dnd.mour.server.statistics.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.domain.repository.query.HeartStatisticsRepository;
import ac.dnd.mour.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mour.server.heart.domain.repository.query.spec.PersonalHeartStatisticsCondition;
import ac.dnd.mour.server.heart.domain.repository.query.spec.StatisticsStandard;
import ac.dnd.mour.server.statistics.application.usecase.query.GetPersonalHeartStatistics;
import ac.dnd.mour.server.statistics.application.usecase.query.response.PersonalHeartStatisticsResponse;
import ac.dnd.mour.server.statistics.application.usecase.query.response.PersonalHeartSummary;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetPersonalHeartStatisticsUseCase {
    private final HeartStatisticsRepository heartStatisticsRepository;

    @MourReadOnlyTransactional
    public PersonalHeartStatisticsResponse invoke(final GetPersonalHeartStatistics query) {
        final List<PersonalHeartHistory> giveHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, true));
        final List<PersonalHeartHistory> takeHistories = heartStatisticsRepository.fetchPersonalHeartHistories(createGiveOrTakeCondition(query, false));
        return new PersonalHeartStatisticsResponse(toSummary(giveHistories), toSummary(takeHistories));
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

    private List<PersonalHeartSummary> toSummary(final List<PersonalHeartHistory> histories) {
        return histories.stream()
                .map(it -> new PersonalHeartSummary(
                        it.event(),
                        it.relationName(),
                        it.groupName(),
                        it.money(),
                        it.day(),
                        it.memo()
                ))
                .toList();
    }

    // FIXME: 데이터 핸들링 요구사항 변경될 수도 있기 때문에 주석으로 유지
//    private List<PersonalHeartSummary> groupingByEvent(final List<PersonalHeartHistory> histories) {
//        final List<Map<String, List<PersonalHeartSummary>>> result = histories.stream()
//                .collect(Collectors.groupingBy(
//                        it -> isSpecialEvent(it.event()) ? it.event() : "기타",
//                        Collectors.mapping(
//                                it -> new PersonalHeartSummary(
//                                        it.relationName(),
//                                        it.groupName(),
//                                        it.money(),
//                                        it.day(),
//                                        it.memo()
//                                ),
//                                Collectors.toList()
//                        )
//                ))
//                .entrySet().stream()
//                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
//        applyAllEvents(result);
//        return result;
//    }
//
//    private boolean isSpecialEvent(final String event) {
//        return StatisticsCategory.isSpecialEvent(event);
//    }
//
//    private void applyAllEvents(final List<Map<String, List<PersonalHeartSummary>>> result) {
//        final List<String> totalEvents = StatisticsCategory.getTotalEvents();
//        for (final String event : totalEvents) {
//            final boolean eventExists = result.stream().anyMatch(map -> map.containsKey(event));
//            if (!eventExists) {
//                result.add(Map.of(event, List.of()));
//            }
//        }
//    }
}
