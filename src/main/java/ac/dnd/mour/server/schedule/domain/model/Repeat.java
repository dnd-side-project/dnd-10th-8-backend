package ac.dnd.mour.server.schedule.domain.model;

import ac.dnd.mour.server.schedule.exception.ScheduleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;

import static ac.dnd.mour.server.schedule.exception.ScheduleExceptionCode.INVALID_REPEAT_TYPE;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Repeat {
    @Enumerated(STRING)
    @Column(name = "repeat_type", columnDefinition = "VARCHAR(30)")
    private Type type;

    @Column(name = "repeat_finish")
    private LocalDate finish;

    public Repeat(final Type type, final LocalDate finish) {
        this.type = type;
        this.finish = finish;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        EVERY_MONTH("month"),
        EVERY_YEAR("year"),
        ;

        private final String value;

        public static Type from(final String value) {
            return Arrays.stream(values())
                    .filter(it -> it.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new ScheduleException(INVALID_REPEAT_TYPE));
        }
    }
}
