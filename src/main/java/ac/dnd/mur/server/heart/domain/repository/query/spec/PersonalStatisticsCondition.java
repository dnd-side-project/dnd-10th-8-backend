package ac.dnd.mur.server.heart.domain.repository.query.spec;

import ac.dnd.mur.server.global.exception.GlobalException;
import ac.dnd.mur.server.heart.exception.HeartException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static ac.dnd.mur.server.global.exception.GlobalExceptionCode.VALIDATION_ERROR;
import static ac.dnd.mur.server.heart.exception.HeartExceptionCode.INVALID_TYPE;

public record PersonalStatisticsCondition(
        long memberId,
        Type type,
        int year,
        int month,
        boolean give
) {
    public PersonalStatisticsCondition {
        if (type == Type.YEAR && (year < 1900 || year > 2100)) {
            throw new GlobalException(VALIDATION_ERROR);
        }

        if (type == Type.MONTH && (year < 1900 || year > 2100) && (month < 1 || month > 12)) {
            throw new GlobalException(VALIDATION_ERROR);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        YEAR("year"),
        MONTH("month"),
        ;

        private final String value;

        public static Type from(final String value) {
            return Arrays.stream(values())
                    .filter(it -> it.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new HeartException(INVALID_TYPE));
        }
    }
}
