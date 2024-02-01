package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.MEMBER_GROUP_NAME_TOO_LONG;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "member_group")
public class Group {
    private static final int NAME_MAX_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    private Group(final Member member, final String name) {
        this.member = member;
        this.name = name;
    }

    public static Group of(final Member member, final String name) {
        validateName(name);
        return new Group(member, name);
    }

    private static void validateName(final String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new MemberException(MEMBER_GROUP_NAME_TOO_LONG);
        }
    }
}
