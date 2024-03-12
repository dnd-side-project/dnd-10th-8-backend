package ac.dnd.mour.server.heart.presentation.v1.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdateHeartRequest(
        @NotNull(message = "금액은 필수입니다.")
        Long money,

        @NotNull(message = "날짜는 필수입니다.")
        LocalDate day,

        @NotNull(message = "행사 종류는 필수입니다.")
        String event,

        String memo,

        List<String> tags
) {
}
