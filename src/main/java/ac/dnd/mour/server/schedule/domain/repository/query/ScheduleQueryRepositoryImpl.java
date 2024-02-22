package ac.dnd.mour.server.schedule.domain.repository.query;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.schedule.domain.repository.query.response.CalendarSchedule;
import ac.dnd.mour.server.schedule.domain.repository.query.response.QCalendarSchedule;
import ac.dnd.mour.server.schedule.domain.repository.query.response.QScheduleDetails;
import ac.dnd.mour.server.schedule.domain.repository.query.response.QScheduleForAlarm;
import ac.dnd.mour.server.schedule.domain.repository.query.response.QUnrecordedSchedule;
import ac.dnd.mour.server.schedule.domain.repository.query.response.ScheduleDetails;
import ac.dnd.mour.server.schedule.domain.repository.query.response.ScheduleForAlarm;
import ac.dnd.mour.server.schedule.domain.repository.query.response.UnrecordedSchedule;
import ac.dnd.mour.server.schedule.domain.repository.query.spec.SearchCalendarScheduleCondition;
import ac.dnd.mour.server.schedule.exception.ScheduleException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mour.server.group.domain.model.QGroup.group;
import static ac.dnd.mour.server.relation.domain.model.QRelation.relation;
import static ac.dnd.mour.server.schedule.domain.model.QSchedule.schedule;
import static ac.dnd.mour.server.schedule.exception.ScheduleExceptionCode.SCHEDULE_NOT_FOUND;

@Repository
@MourReadOnlyTransactional
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public List<UnrecordedSchedule> fetchUnrecordedSchedules(final List<Long> scheduleIds) {
        return query
                .select(new QUnrecordedSchedule(
                        schedule.id,
                        relation.id,
                        relation.name,
                        group.id,
                        group.name,
                        schedule.day,
                        schedule.event,
                        schedule.time,
                        schedule.link,
                        schedule.location
                ))
                .from(schedule)
                .innerJoin(relation).on(relation.id.eq(schedule.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(schedule.id.in(scheduleIds))
                .orderBy(schedule.id.asc())
                .fetch();
    }

    @Override
    public List<CalendarSchedule> fetchCalendarSchedules(final SearchCalendarScheduleCondition condition) {
        return query
                .select(new QCalendarSchedule(
                        schedule.id,
                        relation.id,
                        relation.name,
                        group.id,
                        group.name,
                        schedule.day,
                        schedule.event,
                        schedule.time,
                        schedule.link,
                        schedule.location,
                        schedule.memo
                ))
                .from(schedule)
                .innerJoin(relation).on(relation.id.eq(schedule.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        schedule.memberId.eq(condition.memberId()),
                        scheduleDayInclude(condition.year(), condition.month())
                )
                .orderBy(schedule.day.asc())
                .fetch();
    }

    private BooleanExpression scheduleDayInclude(final int year, final int month) {
        final LocalDate start = LocalDate.of(year, month, 1);
        final LocalDate end = start.plusMonths(1);

        return schedule.day.goe(start).and(schedule.day.lt(end));
    }

    @Override
    public List<ScheduleForAlarm> fetchSchedulesForAlarm(final long memberId) {
        return query
                .select(new QScheduleForAlarm(
                        schedule.id,
                        relation.id,
                        relation.name,
                        group.id,
                        group.name,
                        schedule.day,
                        schedule.event,
                        schedule.repeat,
                        schedule.alarm,
                        schedule.time,
                        schedule.link,
                        schedule.location,
                        schedule.memo
                ))
                .from(schedule)
                .innerJoin(relation).on(relation.id.eq(schedule.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(schedule.memberId.eq(memberId))
                .orderBy(schedule.id.asc())
                .fetch();
    }

    @Override
    public ScheduleDetails fetchScheduleDetails(final long scheduleId, final long memberId) {
        final ScheduleDetails result = query
                .select(new QScheduleDetails(
                        schedule.id,
                        relation.id,
                        relation.name,
                        group.id,
                        group.name,
                        schedule.day,
                        schedule.event,
                        schedule.repeat,
                        schedule.alarm,
                        schedule.time,
                        schedule.link,
                        schedule.location,
                        schedule.memo
                ))
                .from(schedule)
                .innerJoin(relation).on(relation.id.eq(schedule.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        schedule.id.eq(scheduleId),
                        schedule.memberId.eq(memberId)
                )
                .orderBy(schedule.id.asc())
                .fetchOne();

        if (result == null) {
            throw new ScheduleException(SCHEDULE_NOT_FOUND);
        }
        return result;
    }
}
