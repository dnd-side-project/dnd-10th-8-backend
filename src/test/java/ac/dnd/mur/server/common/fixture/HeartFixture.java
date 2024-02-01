package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.heart.domain.model.Heart;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum HeartFixture {
    ;

    private final boolean give;
    private final long money;
    private final LocalDate day;
    private final String event;
    private final String memo;
    private final List<String> tags;

    public Heart toDomain(final Member member, final Relation relation) {
        return new Heart(member, relation, give, money, day, event, memo, tags);
    }
}
