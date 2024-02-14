package ac.dnd.mour.server.statistics.application.usecase;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.global.annotation.UseCase;
import ac.dnd.mour.server.heart.domain.repository.query.HeartStatisticsRepository;
import ac.dnd.mour.server.heart.domain.repository.query.response.TrendHeartStatistics;
import ac.dnd.mour.server.heart.domain.repository.query.spec.TrendHeartStatisticsCondition;
import ac.dnd.mour.server.heart.domain.repository.query.spec.TrendRange;
import ac.dnd.mour.server.statistics.application.usecase.query.GetTrendHeartAverageStatistics;
import ac.dnd.mour.server.statistics.application.usecase.query.response.TrendHeartAverageSummary;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetTrendHeartAverageStatisticsUseCase {
    private final HeartStatisticsRepository heartStatisticsRepository;

    @MourReadOnlyTransactional
    public List<TrendHeartAverageSummary> invoke(final GetTrendHeartAverageStatistics query) {
        final TrendHeartStatisticsCondition condition = new TrendHeartStatisticsCondition(
                query.gender(),
                TrendRange.from(query.range())
        );
        final List<TrendHeartStatistics> result = heartStatisticsRepository.fetchTrendHeartAveragePerEvent(condition);
        return toSummary(result);
    }

    private List<TrendHeartAverageSummary> toSummary(final List<TrendHeartStatistics> result) {
        return result.stream()
                .map(it -> new TrendHeartAverageSummary(it.event(), BigDecimal.valueOf(it.average())))
                .toList();
    }

    // FIXME: 데이터 핸들링 요구사항 변경될 수도 있기 때문에 주석으로 유지
//    private Map<String, BigDecimal> groupingByEvent(final List<TrendHeartStatistics> histories) {
//        final Map<String, BigDecimal> result = histories.stream()
//                .collect(Collectors.groupingBy(
//                        it -> isSpecialEvent(it.event()) ? it.event() : "기타",
//                        Collectors.collectingAndThen(
//                                Collectors.averagingDouble(TrendHeartStatistics::average),
//                                BigDecimal::valueOf
//                        )
//                ));
//        applyAllEvents(result);
//        return result;
//    }
//
//    private boolean isSpecialEvent(final String event) {
//        return StatisticsCategory.isSpecialEvent(event);
//    }
//
//    private void applyAllEvents(final Map<String, BigDecimal> result) {
//        final List<String> totalEvents = StatisticsCategory.getTotalEvents();
//        for (final String event : totalEvents) {
//            result.put(event, result.getOrDefault(event, BigDecimal.ZERO));
//        }
//    }
}
