package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.member.domain.model.Member;
import ac.dnd.mur.server.relation.domain.model.Relation;
import ac.dnd.mur.server.schedule.domain.model.Repeat;
import ac.dnd.mur.server.schedule.domain.model.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ac.dnd.mur.server.schedule.domain.model.Repeat.Type.EVERY_YEAR;

@Getter
@RequiredArgsConstructor
public enum ScheduleFixture {
    결혼식(
            LocalDate.of(2024, 1, 10), "결혼", null,
            LocalDateTime.of(2024, 1, 10, 9, 0), LocalTime.of(16, 0),
            null, "신라호텔", "~~~ 결혼식..."
    ),
    친구_XXX_생일(
            LocalDate.of(2024, 2, 10), "친구 생일", new Repeat(EVERY_YEAR, null),
            LocalDateTime.of(2024, 2, 10, 9, 0), null,
            null, null, "~~~ 생일"
    ),
    특별한_일정_XXX(
            LocalDate.of(2024, 1, 1), "특별한 일정 XXX",
            null, null, null, null, null, null
    ),
    ;

    private final LocalDate day;
    private final String event;
    private final Repeat repeat;
    private final LocalDateTime alarm;
    private final LocalTime time;
    private final String link;
    private final String location;
    private final String memo;

    public Schedule toDomain(final Member member, final Relation relation) {
        return new Schedule(member, relation, day, event, repeat, alarm, time, link, location, memo);
    }

    public Schedule toDomain(final Member member, final Relation relation, final LocalDate day) {
        return new Schedule(member, relation, day, event, repeat, alarm, time, link, location, memo);
    }
}
