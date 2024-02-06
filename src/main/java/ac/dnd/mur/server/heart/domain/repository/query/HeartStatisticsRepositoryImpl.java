package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.response.QPersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.response.QTrendHeartStatistics;
import ac.dnd.mur.server.heart.domain.repository.query.response.TrendHeartStatistics;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalHeartStatisticsCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.StatisticsStandard;
import ac.dnd.mur.server.heart.domain.repository.query.spec.TrendHeartStatisticsCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.TrendRange;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.heart.domain.model.QHeart.heart;
import static ac.dnd.mur.server.member.domain.model.QMember.member;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;

@Repository
@MurReadOnlyTransactional
@RequiredArgsConstructor
public class HeartStatisticsRepositoryImpl implements HeartStatisticsRepository {
    private final JPAQueryFactory query;

    @Override
    public List<PersonalHeartHistory> fetchPersonalHeartHistories(final PersonalHeartStatisticsCondition condition) {
        return query
                .select(new QPersonalHeartHistory(
                        heart.event,
                        relation.name,
                        group.name,
                        heart.money,
                        heart.day,
                        heart.memo
                ))
                .from(heart)
                .innerJoin(relation).on(relation.id.eq(heart.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        heart.memberId.eq(condition.memberId()),
                        isGive(condition.give()),
                        byYearOrMonth(condition.standard(), condition.year(), condition.month())
                )
                .orderBy(heart.day.desc(), heart.id.desc())
                .fetch();
    }

    private BooleanExpression isGive(final boolean give) {
        return give ? heart.give.isTrue() : heart.give.isFalse();
    }

    private BooleanExpression byYearOrMonth(final StatisticsStandard standard, final int year, final int month) {
        if (standard == StatisticsStandard.YEAR) {
            final LocalDate start = LocalDate.of(year, 1, 1);
            final LocalDate end = start.plusYears(1);
            return heart.day.goe(start).and(heart.day.lt(end));
        }

        final LocalDate start = LocalDate.of(year, month, 1);
        final LocalDate end = start.plusMonths(1);
        return heart.day.goe(start).and(heart.day.lt(end));
    }

    @Override
    public List<TrendHeartStatistics> fetchTrendHeartAveragePerEvent(final TrendHeartStatisticsCondition condition) {
        return query
                .select(new QTrendHeartStatistics(
                        heart.event,
                        heart.money.avg()
                ))
                .from(heart)
                .innerJoin(member).on(member.id.eq(heart.memberId))
                .where(
                        member.gender.eq(condition.gender()),
                        member.birth.between(getTrendRangeStart(condition.range()), getTrendRangeEnd(condition.range()))
                )
                .groupBy(heart.event)
                .fetch();
    }

    private LocalDate getTrendRangeStart(final TrendRange range) {
        final int value = range.getValue() + 9;
        return LocalDate.now().minusYears(value);
    }

    private LocalDate getTrendRangeEnd(final TrendRange range) {
        final int value = range.getValue();
        return LocalDate.now().minusYears(value);
    }
}
