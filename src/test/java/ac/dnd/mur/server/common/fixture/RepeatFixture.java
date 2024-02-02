package ac.dnd.mur.server.common.fixture;

import ac.dnd.mur.server.schedule.domain.model.Repeat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static ac.dnd.mur.server.schedule.domain.model.Repeat.Type.EVERY_MONTH;
import static ac.dnd.mur.server.schedule.domain.model.Repeat.Type.EVERY_YEAR;

@Getter
@RequiredArgsConstructor
public enum RepeatFixture {
    매달_반복_종료_X(EVERY_MONTH, null),
    매달_반복_종료_2024_12_31(EVERY_MONTH, LocalDate.of(2024, 12, 31)),
    매년_반복_종료_X(EVERY_YEAR, null),
    매년_반복_종료_2026_12_31(EVERY_YEAR, LocalDate.of(2026, 12, 31)),
    ;

    private final Repeat.Type type;
    private final LocalDate finish;

    public Repeat toDomain() {
        return new Repeat(type, finish);
    }
}
