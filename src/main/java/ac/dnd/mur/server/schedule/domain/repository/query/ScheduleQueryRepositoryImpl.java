package ac.dnd.mur.server.schedule.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.schedule.domain.repository.query.response.QUnrecordedSchedule;
import ac.dnd.mur.server.schedule.domain.repository.query.response.UnrecordedSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;
import static ac.dnd.mur.server.schedule.domain.model.QSchedule.schedule;

@Repository
@MurReadOnlyTransactional
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
}
