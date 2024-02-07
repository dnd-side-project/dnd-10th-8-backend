package ac.dnd.mur.server.schedule.domain.repository.query.response;

import ac.dnd.mur.server.schedule.domain.model.Repeat;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleForAlarm(
        long id,
        long relationId,
        String relationName,
        long groupid,
        String groupName,
        LocalDate day,
        String event,
        Repeat repeat,
        LocalDateTime alarm,
        LocalTime time,
        String link,
        String location,
        String memo
) {
    @QueryProjection
    public ScheduleForAlarm {
    }
}
