package ac.dnd.mur.server.schedule.domain.repository.query.spec;

public record SearchCalendarScheduleCondition(
        long memberId,
        int year,
        int month
) {
}
