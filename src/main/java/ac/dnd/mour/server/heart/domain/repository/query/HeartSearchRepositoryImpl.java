package ac.dnd.mour.server.heart.domain.repository.query;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mour.server.heart.domain.repository.query.response.QHeartHistory_MoneySummary;
import ac.dnd.mour.server.heart.domain.repository.query.response.QHeartHistory_RelationInfo;
import ac.dnd.mour.server.heart.domain.repository.query.spec.SearchHeartCondition;
import ac.dnd.mour.server.heart.domain.repository.query.spec.SearchSpecificRelationHeartCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ac.dnd.mour.server.group.domain.model.QGroup.group;
import static ac.dnd.mour.server.heart.domain.model.QHeart.heart;
import static ac.dnd.mour.server.heart.domain.repository.query.spec.SearchHeartCondition.Sort.INTIMACY;
import static ac.dnd.mour.server.relation.domain.model.QRelation.relation;

@Repository
@MourReadOnlyTransactional
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
        final List<HeartHistory.RelationInfo> memberRelations = query
                .select(new QHeartHistory_RelationInfo(
                        relation.id,
                        relation.name,
                        group.id,
                        group.name
                ))
                .from(relation)
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        relation.memberId.eq(condition.memberId()),
                        relationNameEq(condition.name())
                )
                .orderBy(relation.id.desc())
                .fetch();

        if (memberRelations.isEmpty()) {
            return List.of();
        }

        final List<HeartHistory.RelationInfo> hearts = query
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
                        relation.memberId.eq(condition.memberId()),
                        relationNameEq(condition.name())
                )
                .orderBy(heart.id.desc())
                .fetch()
                .stream()
                .distinct()
                .toList();

        List<HeartHistory> result = conbineMoneyHistories(hearts);
        if (condition.sort() == INTIMACY) {
            result = sortWithIntimacy(result);
        }

        return fillNonInteractionRelations(result, memberRelations);
    }

    private List<HeartHistory> conbineMoneyHistories(final List<HeartHistory.RelationInfo> hearts) {
        final List<Long> relationIds = hearts.stream()
                .map(HeartHistory.RelationInfo::relationId)
                .toList();
        final List<HeartHistory.MoneySummary> giveMoneySummaries = fetchMoneySummaries(relationIds, true);
        final List<HeartHistory.MoneySummary> takeMoneySummaries = fetchMoneySummaries(relationIds, false);

        final List<HeartHistory> result = new ArrayList<>();
        hearts.forEach(it -> {
            final List<Long> giveHistories = extractMoneyHistories(giveMoneySummaries, it.relationId());
            final List<Long> takeHistories = extractMoneyHistories(takeMoneySummaries, it.relationId());
            result.add(new HeartHistory(
                    it.relationId(),
                    it.relationName(),
                    it.groupid(),
                    it.groupName(),
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

    private List<HeartHistory> fillNonInteractionRelations(
            final List<HeartHistory> result,
            final List<HeartHistory.RelationInfo> memberRelations
    ) {
        final List<Long> interactionRelationIds = result.stream()
                .map(HeartHistory::relationId)
                .toList();

        final List<HeartHistory.RelationInfo> nonInteractionRelationIds = memberRelations.stream()
                .filter(it -> !interactionRelationIds.contains(it.relationId()))
                .toList();

        nonInteractionRelationIds.forEach(it -> result.add(new HeartHistory(
                it.relationId(),
                it.relationName(),
                it.groupid(),
                it.groupName(),
                List.of(),
                List.of()
        )));
        return result;
    }

    private BooleanExpression relationNameEq(final String name) {
        if (StringUtils.hasText(name)) {
            return relation.name.contains(name);
        }
        return null;
    }
}
