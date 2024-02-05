package ac.dnd.mur.server.heart.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.heart.domain.repository.query.response.QHeartHistory_MoneySummary;
import ac.dnd.mur.server.heart.domain.repository.query.response.QHeartHistory_RelationInfo;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mur.server.heart.domain.repository.query.spec.SearchSpecificRelationHeartCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.heart.domain.model.QHeart.heart;
import static ac.dnd.mur.server.heart.domain.repository.query.spec.SearchHeartCondition.Sort.INTIMACY;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;

@Repository
@MurReadOnlyTransactional
@RequiredArgsConstructor
public class HeartSearchRepositoryImpl implements HeartSearchRepository {
    private final JPAQueryFactory query;

    @Override
    public List<Heart> fetchHeartsWithSpecificRelation(final SearchSpecificRelationHeartCondition condition) {
        return query
                .select(heart)
                .from(heart)
                .leftJoin(heart.tags).fetchJoin()
                .where(
                        heart.memberId.eq(condition.memberId()),
                        heart.relationId.eq(condition.relationId())
                )
                .orderBy(decideOrderingBySort(condition.sort()))
                .fetch();
    }

    private static OrderSpecifier<Long> decideOrderingBySort(final SearchSpecificRelationHeartCondition.Sort sort) {
        if (sort == SearchSpecificRelationHeartCondition.Sort.RECENT) {
            return heart.id.desc();
        }
        return heart.id.asc();
    }

    @Override
    public List<HeartHistory> fetchHeartsByCondition(final SearchHeartCondition condition) {
        final List<HeartHistory.RelationInfo> relations = query
                .select(new QHeartHistory_RelationInfo(
                        relation.id,
                        relation.name,
                        group.id,
                        group.name
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

        if (relations.isEmpty()) {
            return List.of();
        }

        final List<HeartHistory> result = conbineMoneyHistories(relations);

        if (condition.sort() == INTIMACY) {
            return sortWithIntimacy(result);
        }
        return result;
    }

    private List<HeartHistory> conbineMoneyHistories(final List<HeartHistory.RelationInfo> relations) {
        final List<Long> relationIds = relations.stream()
                .map(HeartHistory.RelationInfo::relationId)
                .toList();
        final List<HeartHistory.MoneySummary> giveMoneySummaries = fetchMoneySummaries(relationIds, true);
        final List<HeartHistory.MoneySummary> takeMoneySummaries = fetchMoneySummaries(relationIds, false);

        final List<HeartHistory> result = new ArrayList<>();
        relations.forEach(relationInfo -> {
            final List<Long> giveHistories = extractMoneyHistories(giveMoneySummaries, relationInfo.relationId());
            final List<Long> takeHistories = extractMoneyHistories(takeMoneySummaries, relationInfo.relationId());
            result.add(new HeartHistory(
                    relationInfo.relationId(),
                    relationInfo.relationName(),
                    relationInfo.groupid(),
                    relationInfo.groupName(),
                    giveHistories,
                    takeHistories
            ));
        });
        return result;
    }

    private List<HeartHistory.MoneySummary> fetchMoneySummaries(final List<Long> relationIds, final boolean give) {
        return query
                .select(new QHeartHistory_MoneySummary(
                        heart.relationId,
                        heart.money
                ))
                .from(heart)
                .where(
                        heart.relationId.in(relationIds),
                        give ? heart.give.isTrue() : heart.give.isFalse()
                )
                .fetch();
    }

    private List<Long> extractMoneyHistories(final List<HeartHistory.MoneySummary> moneySummaries, final long relationId) {
        return moneySummaries.stream()
                .filter(it -> it.relationId() == relationId)
                .map(HeartHistory.MoneySummary::money)
                .toList();
    }

    private List<HeartHistory> sortWithIntimacy(final List<HeartHistory> result) {
        return result.stream()
                .sorted((o1, o2) -> Long.compare(totalExchangeMoney(o2), totalExchangeMoney(o1)))
                .toList();
    }

    private long totalExchangeMoney(final HeartHistory history) {
        return Stream.concat(history.giveHistories().stream(), history.takeHistories().stream())
                .mapToLong(it -> it)
                .sum();
    }

    private BooleanExpression relationNameEq(final String name) {
        if (StringUtils.hasText(name)) {
            return relation.name.eq(name);
        }
        return null;
    }
}
