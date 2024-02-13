package ac.dnd.mour.server.schedule.domain.repository.query.response;

import ac.dnd.mour.server.schedule.domain.model.Repeat;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleDetails(
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
    public ScheduleDetails {
    }
}
