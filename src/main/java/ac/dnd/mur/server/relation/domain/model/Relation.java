package ac.dnd.mur.server.relation.domain.model;

import ac.dnd.mur.server.global.base.BaseEntity;
import ac.dnd.mur.server.group.domain.model.Group;
import ac.dnd.mur.server.member.domain.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "relation")
public class Relation extends BaseEntity<Relation> {
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Lob
    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    public Relation(
            final Member member,
            final Group group,
            final String name,
            final String imageUrl,
            final String memo
    ) {
        this.memberId = member.getId();
        this.groupId = group.getId();
        this.name = name;
        this.imageUrl = imageUrl;
        this.memo = memo;
    }

    public void update(
            final Group group,
            final String name,
            final String imageUrl,
            final String memo
    ) {
        this.groupId = group.getId();
        this.name = name;
        this.imageUrl = imageUrl;
        this.memo = memo;
    }
}
