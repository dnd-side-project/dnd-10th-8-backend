package ac.dnd.mour.server.heart.domain.repository.query.spec;

import ac.dnd.mour.server.global.exception.GlobalException;

import static ac.dnd.mour.server.global.exception.GlobalExceptionCode.VALIDATION_ERROR;

public record PersonalHeartStatisticsCondition(
        long memberId,
        StatisticsStandard standard,
        int year,
        int month,
        boolean give
) {
    public PersonalHeartStatisticsCondition {
        if (standard == StatisticsStandard.YEAR && (year < 1900 || year > 2100)) {
            throw new GlobalException(VALIDATION_ERROR);
        }

        if (standard == StatisticsStandard.MONTH && (year < 1900 || year > 2100) && (month < 1 || month > 12)) {
            throw new GlobalException(VALIDATION_ERROR);
        }
    }
}
