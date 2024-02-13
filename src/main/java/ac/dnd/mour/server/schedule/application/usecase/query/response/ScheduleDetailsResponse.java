package ac.dnd.mour.server.schedule.application.usecase.query.response;

import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.relation.domain.model.response.RelationSummary;
import ac.dnd.mour.server.schedule.domain.repository.query.response.ScheduleDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleDetailsResponse(
        long id,
        RelationSummary relation,
        LocalDate day,
        String event,
        String repeatType,
        LocalDate repeatFinish,
        LocalDateTime alarm,
        LocalTime time,
        String link,
        String location,
        String memo
) {
    public static ScheduleDetailsResponse from(final ScheduleDetails schedule) {
        return new ScheduleDetailsResponse(
                schedule.id(),
                new RelationSummary(
                        schedule.relationId(),
                        schedule.relationName(),
                        new GroupResponse(schedule.groupid(), schedule.groupName())
                ),
                schedule.day(),
                schedule.event(),
                (schedule.repeat() != null) ? schedule.repeat().getType().getValue() : null,
                (schedule.repeat() != null) ? schedule.repeat().getFinish() : null,
                schedule.alarm(),
                schedule.time(),
                schedule.link(),
                schedule.location(),
                schedule.memo()
        );
    }
}
