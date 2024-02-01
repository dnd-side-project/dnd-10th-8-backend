package ac.dnd.mur.server.schedule.domain.model;

import ac.dnd.mur.server.global.base.BaseEntity;
import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "schedule")
public class Schedule extends BaseEntity<Schedule> {
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "relation_id", nullable = false)
    private Long relationId;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Column(name = "event", nullable = false)
    private String event;

    @Column(name = "alarm")
    private LocalDateTime alarm;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "link")
    private String link;

    @Column(name = "location")
    private String location;

    @Lob
    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    public Schedule(
            final Member member,
            final Relation relation,
            final LocalDate day,
            final String event,
            final LocalDateTime alarm,
            final LocalTime time,
            final String link,
            final String location,
            final String memo
    ) {
        this.memberId = member.getId();
        this.relationId = relation.getId();
        this.day = day;
        this.event = event;
        this.alarm = alarm;
        this.time = time;
        this.link = link;
        this.location = location;
        this.memo = memo;
    }
}
