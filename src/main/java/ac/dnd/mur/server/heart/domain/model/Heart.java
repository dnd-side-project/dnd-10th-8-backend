package ac.dnd.mur.server.heart.domain.model;

import ac.dnd.mur.server.global.base.BaseEntity;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "heart")
public class Heart extends BaseEntity<Heart> {
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "relation_id", nullable = false)
    private Long relationId;

    @Column(name = "is_give", nullable = false, columnDefinition = "TINYINT")
    private boolean give;

    @Column(name = "amount", nullable = false)
    private long money;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Column(name = "event", nullable = false)
    private String event;

    @Lob
    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @OneToMany(mappedBy = "heart", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private final List<Tag> tags = new ArrayList<>();

    public Heart(
            final Member member,
            final Relation relation,
            final boolean give,
            final long money,
            final LocalDate day,
            final String event,
            final String memo,
            final List<String> tags
    ) {
        this.memberId = member.getId();
        this.relationId = relation.getId();
        this.give = give;
        this.money = money;
        this.day = day;
        this.event = event;
        this.memo = memo;
        applyTag(tags);
    }

    private void applyTag(final List<String> tags) {
        this.tags.clear();

        if (!CollectionUtils.isEmpty(tags)) {
            this.tags.addAll(
                    tags.stream()
                            .map(it -> new Tag(this, it))
                            .toList()
            );
        }
    }
}
