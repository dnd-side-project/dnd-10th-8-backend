package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RelationFixture {
    친구_1("관계-친구1", "https://친구-1-url", "메모..."),
    친구_2("관계-친구2", "https://친구-2-url", "메모..."),
    친구_3("관계-친구3", "https://친구-3-url", "메모..."),

    가족_1("관계-가족1", "https://가족-1-url", "메모..."),
    가족_2("관계-가족2", "https://가족-2-url", "메모..."),
    가족_3("관계-가족3", "https://가족-3-url", "메모..."),

    지인_1("관계-지인1", "https://지인-1-url", "메모..."),
    지인_2("관계-지인2", "https://지인-2-url", "메모..."),
    지인_3("관계-지인3", "https://지인-3-url", "메모..."),

    직장_1("관계-직장1", "https://직장-1-url", "메모..."),
    직장_2("관계-직장2", "https://직장-2-url", "메모..."),
    직장_3("관계-직장3", "https://직장-3-url", "메모..."),

    거래처_1("관계-거래처1", "https://거래처-1-url", "메모..."),
    거래처_2("관계-거래처2", "https://거래처-2-url", "메모..."),
    거래처_3("관계-거래처3", "https://거래처-3-url", "메모..."),
    ;

    private final String name;
    private final String imageUrl;
    private final String memo;

    public Relation toDomain(final Member member, final Group group) {
        return new Relation(member, group, name, imageUrl, memo);
    }
}
