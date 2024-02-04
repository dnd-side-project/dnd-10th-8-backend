package ac.dnd.mur.server.heart.domain.repository.query.spec;

import ac.dnd.mur.server.heart.exception.HeartException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static ac.dnd.mur.server.heart.exception.HeartExceptionCode.INVALID_SORT;

public record SearchSpecificRelationHeartCondition(
        long memberId,
        long relationId,
        Sort sort
) {
    @Getter
    @RequiredArgsConstructor
    public enum Sort {
        RECENT("recent"),
        OLD("old"),
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
