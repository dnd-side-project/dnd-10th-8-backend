package ac.dnd.mur.server.schedule.domain.repository.query;

import ac.dnd.mur.server.schedule.domain.repository.query.response.UnrecordedSchedule;

import java.util.List;

public interface ScheduleQueryRepository {
    List<UnrecordedSchedule> fetchUnrecordedSchedules(final List<Long> scheduleIds);
}
