package ac.dnd.mur.server.schedule.application.usecase.query.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.schedule.domain.repository.query.response.UnrecordedSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record UnrecordedScheduleResponse(
        long id,
        RelationSummary relation,
        LocalDate day,
        String event,
        LocalTime time,
        String link,
        String location
) {
    public record RelationSummary(
            long id,
            String name,
            GroupResponse group
    ) {
    }

    public static UnrecordedScheduleResponse from(final UnrecordedSchedule schedule) {
        return new UnrecordedScheduleResponse(
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
