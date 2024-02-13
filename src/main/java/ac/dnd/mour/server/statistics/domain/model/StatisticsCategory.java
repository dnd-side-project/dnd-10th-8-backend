package ac.dnd.mour.server.statistics.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum StatisticsCategory {
    결혼("결혼"),
    생일("생일"),
    출산("출산"),
    돌잔치("돌잔치"),
    개업("개업"),
    기타("기타"),
    ;

    private final String value;

    public static boolean isSpecialEvent(final String event) {
        return Stream.of(결혼, 생일, 출산, 돌잔치, 개업)
                .anyMatch(it -> it.value.equals(event));
    }

    public static List<String> getTotalEvents() {
        return Arrays.stream(values())
                .map(StatisticsCategory::getValue)
                .toList();
    }
}
