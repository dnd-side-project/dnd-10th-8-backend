package ac.dnd.mur.server.schedule.application.usecase.command;

import ac.dnd.mur.server.schedule.domain.model.Repeat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateScheduleCommand(
        long memberId,
        long relationId,
        LocalDate day,
        String event,
        Repeat repeat,
        LocalDateTime alarm,
        LocalTime time,
        String link,
        String location,
        String memo
) {
}
