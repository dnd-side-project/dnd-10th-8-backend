package ac.dnd.mur.server.heart.domain.repository.query.spec;

import ac.dnd.mur.server.heart.exception.HeartException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static ac.dnd.mur.server.heart.exception.HeartExceptionCode.INVALID_SORT;

public record SearchHeartCondition(
        long memberId,
        Sort sort,
        String name
) {
    @Getter
    @RequiredArgsConstructor
    public enum Sort {
        RECENT("recent"),
        INTIMACY("intimacy"),
        ;

        private final String value;

        public static Sort from(final String value) {
            return Arrays.stream(values())
                    .filter(it -> it.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new HeartException(INVALID_SORT));
        }
    }
}
