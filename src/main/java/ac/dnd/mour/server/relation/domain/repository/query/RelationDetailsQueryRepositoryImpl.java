package ac.dnd.mour.server.relation.domain.repository.query;

import ac.dnd.mour.server.global.annotation.MourReadOnlyTransactional;
import ac.dnd.mour.server.relation.domain.repository.query.response.QRelationDetails;
import ac.dnd.mour.server.relation.domain.repository.query.response.QRelationSummary;
import ac.dnd.mour.server.relation.domain.repository.query.response.RelationDetails;
import ac.dnd.mour.server.relation.domain.repository.query.response.RelationSummary;
import ac.dnd.mour.server.relation.exception.RelationException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static ac.dnd.mour.server.group.domain.model.QGroup.group;
import static ac.dnd.mour.server.relation.domain.model.QRelation.relation;
import static ac.dnd.mour.server.relation.exception.RelationExceptionCode.RELATION_NOT_FOUND;

@Repository
@MourReadOnlyTransactional
@RequiredArgsConstructor
public class RelationDetailsQueryRepositoryImpl implements RelationDetailsQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public RelationDetails fetchRelation(final long id, final long memberId) {
        final RelationDetails result = query
                .select(new QRelationDetails(
                        relation.id,
                        relation.name,
                        relation.imageUrl,
                        relation.memo,
                        group.id,
                        group.name
                ))
                .from(relation)
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        relation.id.eq(id),
                        relation.memberId.eq(memberId)
                )
                .fetchOne();

        if (result == null) {
            throw new RelationException(RELATION_NOT_FOUND);
        }
        return result;
    }

    @Override
    public List<RelationSummary> fetchRelations(final long memberId, final String name) {
        return query
                .select(new QRelationSummary(
                        relation.id,
                        relation.name,
                        group.id,
                        group.name
                ))
                .from(relation)
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(
                        relation.memberId.eq(memberId),
                        relationNameEq(name)
                )
                .orderBy(relation.id.desc())
                .fetch();
    }

    private BooleanExpression relationNameEq(final String name) {
        if (StringUtils.hasText(name)) {
            return relation.name.eq(name);
        }
        return null;
    }
}
