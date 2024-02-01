package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.member.exception.MemberException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_GROUP_ALREADY_EXISTS;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Groups {
    private static final List<String> defaultGroups = List.of("친구", "가족", "지인", "직장");

    @OneToMany(mappedBy = "member", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<Group> groups = new ArrayList<>();

    private Groups(final List<Group> groups) {
        this.groups = groups;
    }

    public static Groups init(final Member member) {
        final List<Group> groups = defaultGroups.stream()
                .map(it -> Group.of(member, it))
                .toList();
        return new Groups(new ArrayList<>(groups));
    }

    public void add(final Member member, final String name) {
        if (existsName(name)) {
            throw new MemberException(MEMBER_GROUP_ALREADY_EXISTS);
        }

        this.groups.add(Group.of(member, name));
    }

    private boolean existsName(final String name) {
        return groups.stream()
                .anyMatch(it -> it.getName().equals(name));
    }
}
