package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RelationFixture {
    친구_1("친구1", "010-0000-0001", "메모..."),
    친구_2("친구2", "010-0000-0002", "메모..."),
    친구_3("친구3", "010-0000-0003", "메모..."),

    가족_1("가족1", "010-0001-0001", "메모..."),
    가족_2("가족2", "010-0001-0002", "메모..."),
    가족_3("가족3", "010-0001-0003", "메모..."),

    지인_1("지인1", "010-0002-0001", "메모..."),
    지인_2("지인2", "010-0002-0002", "메모..."),
    지인_3("지인3", "010-0002-0003", "메모..."),

    직장_1("직장1", "010-0003-0001", "메모..."),
    직장_2("직장2", "010-0003-0002", "메모..."),
    직장_3("직장3", "010-0003-0003", "메모..."),

    거래처_1("거래처1", "010-0004-0001", "메모..."),
    거래처_2("거래처2", "010-0004-0002", "메모..."),
    거래처_3("거래처3", "010-0004-0003", "메모..."),
    ;

    private final String name;
    private final String phone;
    private final String memo;

    public Relation toDomain(final Member member, final Group group) {
        return new Relation(member, group, name, phone, memo);
    }
}
