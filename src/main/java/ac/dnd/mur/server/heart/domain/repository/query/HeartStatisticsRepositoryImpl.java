package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.heart.domain.repository.query.response.PersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.response.QPersonalHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.PersonalStatisticsCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.heart.domain.model.QHeart.heart;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;

@Repository
@MurReadOnlyTransactional
@RequiredArgsConstructor
public class HeartStatisticsRepositoryImpl implements HeartStatisticsRepository {
    private final JPAQueryFactory query;

    @Override
    public List<PersonalHeartHistory> fetchPersonalHeartHistories(final PersonalStatisticsCondition condition) {
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
                        byYearOrMonth(condition.type(), condition.year(), condition.month())
                )
                .orderBy(heart.day.desc(), heart.id.desc())
                .fetch();
    }

    private BooleanExpression isGive(final boolean give) {
        return give ? heart.give.isTrue() : heart.give.isFalse();
    }

    private BooleanExpression byYearOrMonth(final PersonalStatisticsCondition.Type type, final int year, final int month) {
        if (type == PersonalStatisticsCondition.Type.YEAR) {
            final LocalDate start = LocalDate.of(year, 1, 1);
            final LocalDate end = start.plusYears(1);
            return heart.day.goe(start).and(heart.day.lt(end));
        }

        final LocalDate start = LocalDate.of(year, month, 1);
        final LocalDate end = start.plusMonths(1);
        return heart.day.goe(start).and(heart.day.lt(end));
    }
}
