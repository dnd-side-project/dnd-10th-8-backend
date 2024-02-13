package ac.dnd.mour.server.schedule.application.usecase.query.response;

import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.relation.domain.model.response.RelationSummary;
import ac.dnd.mour.server.schedule.domain.repository.query.response.CalendarSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarScheduleResponse(
        long id,
        RelationSummary relation,
        LocalDate day,
        String event,
        LocalTime time,
        String link,
        String location,
        String memo
) {
    public static CalendarScheduleResponse from(final CalendarSchedule schedule) {
        return new CalendarScheduleResponse(
                schedule.id(),
                new RelationSummary(
                        schedule.relationId(),
                        schedule.relationName(),
                        new GroupResponse(schedule.groupid(), schedule.groupName())
                ),
                schedule.day(),
                schedule.event(),
                schedule.time(),
                schedule.link(),
                schedule.location(),
                schedule.memo()
        );
    }
}
