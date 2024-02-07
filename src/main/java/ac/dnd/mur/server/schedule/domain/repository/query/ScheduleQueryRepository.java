package ac.dnd.mur.server.schedule.domain.repository.query;

import ac.dnd.mur.server.schedule.domain.repository.query.response.CalendarSchedule;
import ac.dnd.mur.server.schedule.domain.repository.query.response.ScheduleForAlarm;
import ac.dnd.mur.server.schedule.domain.repository.query.response.UnrecordedSchedule;
import ac.dnd.mur.server.schedule.domain.repository.query.spec.SearchCalendarScheduleCondition;

import java.util.List;

public interface ScheduleQueryRepository {
    List<UnrecordedSchedule> fetchUnrecordedSchedules(final List<Long> scheduleIds);

    List<CalendarSchedule> fetchCalendarSchedules(final SearchCalendarScheduleCondition condition);

    List<ScheduleForAlarm> fetchSchedulesForAlarm(final long memberId);
}
