package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.response.QHeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.heart.domain.model.QHeart.heart;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition.Sort.INTIMACY;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@MurReadOnlyTransactional
@RequiredArgsConstructor
public class HeartSearchRepositoryImpl implements HeartSearchRepository {
    private final JPAQueryFactory query;

    @Override
    public List<HeartHistory> fetchHeartsByCondition(final SearchHeartCondition condition) {
        // TODO SubQuery + 1 Query vs 분리 + 3 Query -> 성능 테스트
        final List<HeartHistory> result = query
                .select(new QHeartHistory(
                        relation.id,
                        relation.name,
                        group.id,
                        group.name,
                        fetchGivenMoney(condition.memberId()),
                        fetchTakenMoney(condition.memberId())
                ))
                .from(heart)
                .innerJoin(relation).on(relation.id.eq(heart.relationId))
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        heart.memberId.eq(condition.memberId()),
                        relationNameEq(condition.name())
                )
                .orderBy(heart.id.desc())
                .fetch()
                .stream()
                .distinct()
                .toList();

        if (condition.sort() == INTIMACY) {
            return sortWithIntimacy(result);
        }
        return result;
    }

    private List<HeartHistory> sortWithIntimacy(final List<HeartHistory> result) {
        return result.stream()
                .sorted((o1, o2) -> Long.compare(o2.giveMoney() + o2.takeMoney(), o1.giveMoney() + o1.takeMoney()))
                .toList();
    }

    private Expression<Long> fetchGivenMoney(final long memberId) {
        return ExpressionUtils.as(
                select(heart.money.sum())
                        .from(heart)
                        .where(
                                heart.memberId.eq(memberId),
                                heart.relationId.eq(relation.id),
                                heart.give.isTrue()
                        ),
                "giveMoney"
        );
    }

    private Expression<Long> fetchTakenMoney(final long memberId) {
        return ExpressionUtils.as(
                select(heart.money.sum())
                        .from(heart)
                        .where(
                                heart.memberId.eq(memberId),
                                heart.relationId.eq(relation.id),
                                heart.give.isFalse()
                        ),
                "takeMoney"
        );
    }

    private BooleanExpression relationNameEq(final String name) {
        if (StringUtils.hasText(name)) {
            return relation.name.eq(name);
        }
        return null;
    }
}
