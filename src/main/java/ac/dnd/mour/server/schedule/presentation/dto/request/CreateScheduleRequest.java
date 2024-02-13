package ac.dnd.mour.server.schedule.presentation.dto.request;

import ac.dnd.mour.server.schedule.domain.model.Repeat;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateScheduleRequest(
        @NotNull(message = "관계 정보는 필수입니다.")
        Long relationId,

        @NotNull(message = "날짜 정보는 필수입니다.")
        LocalDate day,

        @NotNull(message = "행사 정보는 필수입니다.")
        String event,

        String repeatType,

        LocalDate repeatFinish,

        LocalDateTime alarm,

        LocalTime time,

        String link,

        String location,

        String memo
) {
    public Repeat toRepeat() {
        if (StringUtils.hasText(repeatType)) {
            return new Repeat(Repeat.Type.from(repeatType), repeatFinish);
        }
        return null;
    }
}
