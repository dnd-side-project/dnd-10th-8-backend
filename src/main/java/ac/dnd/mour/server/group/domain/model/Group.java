package ac.dnd.mour.server.group.domain.model;

import ac.dnd.mour.server.global.base.BaseEntity;
import ac.dnd.mour.server.member.domain.model.Member;
import ac.dnd.mour.server.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static ac.dnd.mour.server.member.exception.MemberExceptionCode.MEMBER_GROUP_NAME_TOO_LONG;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "member_group")
public class Group extends BaseEntity<Group> {
    private static final List<String> defaultGroups = List.of("친구", "가족", "지인", "직장");
    private static final int NAME_MAX_LENGTH = 8;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "name", nullable = false)
    private String name;

    private Group(final Member member, final String name) {
        this.memberId = member.getId();
        this.name = name;
    }

    public static Group of(final Member member, final String name) {
        validateName(name);
        return new Group(member, name);
    }

    public static List<Group> init(final Member member) {
        return defaultGroups.stream()
                .map(it -> new Group(member, it))
                .toList();
    }

    private static void validateName(final String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new MemberException(MEMBER_GROUP_NAME_TOO_LONG);
        }
    }

    public void update(final String name) {
        this.name = name;
    }
}
