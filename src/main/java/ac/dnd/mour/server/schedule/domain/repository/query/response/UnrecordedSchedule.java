package ac.dnd.mour.server.schedule.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalTime;

public record UnrecordedSchedule(
        long id,
        long relationId,
        String relationName,
        long groupid,
        String groupName,
        LocalDate day,
        String event,
        LocalTime time,
        String link,
        String location
) {
    @QueryProjection
    public UnrecordedSchedule {
    }
}
