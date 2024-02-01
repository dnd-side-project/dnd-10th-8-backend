package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.member.domain.model.Group;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RelationFixture {
    ;

    private final String name;
    private final String phone;
    private final String memo;

    public Relation toDomain(final Member member, final Group group) {
        return new Relation(member, group, name, phone, memo);
    }
}
