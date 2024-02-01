package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public enum ScheduleFixture {
    ;

    private final LocalDate day;
    private final String event;
    private final LocalDateTime alarm;
    private final LocalTime time;
    private final String link;
    private final String location;
    private final String memo;

    public Schedule toDomain(final Member member, final Relation relation) {
        return new Schedule(
                member,
                relation,
                day,
                event,
                alarm,
                time,
                link,
                location,
                memo
        );
    }
}
