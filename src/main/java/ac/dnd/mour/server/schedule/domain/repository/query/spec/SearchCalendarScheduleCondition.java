package ac.dnd.mour.server.schedule.domain.repository.query.spec;

public record SearchCalendarScheduleCondition(
        long memberId,
        int year,
        int month
) {
}
