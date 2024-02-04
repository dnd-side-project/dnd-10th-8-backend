package ac.dnd.mur.server.relation.domain.repository.query;

import ac.dnd.mur.server.global.annotation.MurReadOnlyTransactional;
import ac.dnd.mur.server.relation.domain.repository.query.response.QRelationDetails;
import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static ac.dnd.mur.server.group.domain.model.QGroup.group;
import static ac.dnd.mur.server.relation.domain.model.QRelation.relation;

@Repository
@MurReadOnlyTransactional
@RequiredArgsConstructor
public class RelationDetailsQueryRepositoryImpl implements RelationDetailsQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public RelationDetails fetchRelation(final long id) {
        return query
                .select(new QRelationDetails(
                        relation.id,
                        relation.name,
                        group.id,
                        group.name
                ))
                .from(relation)
                .innerJoin(group).on(group.id.eq(relation.groupId))
                .where(relation.id.eq(id))
                .fetchOne();
    }
}
