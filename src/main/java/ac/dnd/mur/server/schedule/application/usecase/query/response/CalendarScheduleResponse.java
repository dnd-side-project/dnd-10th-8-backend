package ac.dnd.mur.server.schedule.application.usecase.query.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.relation.domain.model.response.RelationSummary;
import ac.dnd.mur.server.schedule.domain.repository.query.response.CalendarSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarScheduleResponse(
        long id,
        RelationSummary relation,
        LocalDate day,
        String event,
        LocalTime time,
        String link,
        String location
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
                schedule.location()
        );
    }
}
