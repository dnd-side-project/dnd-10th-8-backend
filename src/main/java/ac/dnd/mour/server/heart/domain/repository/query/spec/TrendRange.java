package ac.dnd.mour.server.heart.domain.repository.query.spec;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TrendRange {
    RANGE_20(20),
    RANGE_30(30),
    RANGE_40(40),
    RANGE_50_OVER(50),
    ;

    private final int value;

    public static TrendRange from(final int value) {
        return Arrays.stream(values())
                .filter(it -> it.value == value)
                .findFirst()
                .orElse(RANGE_20);
    }
}
