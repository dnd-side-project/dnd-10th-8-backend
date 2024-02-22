package ac.dnd.mour.server.schedule.domain.service;

import ac.dnd.mour.server.schedule.exception.ScheduleException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static ac.dnd.mour.server.schedule.exception.ScheduleExceptionCode.INVALID_SCHEDULE_DAY;

@Component
public class ScheduleValidator {
    public void validateDay(final LocalDate day) {
        final LocalDate now = LocalDate.now();

        if (day.isBefore(now)) {
            throw new ScheduleException(INVALID_SCHEDULE_DAY);
        }
    }
}
