package ac.dnd.mur.server.heart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ApplyUnrecordedHeartRequest(
        @NotNull(message = "일정 정보는 필수입니다.")
        Long scheduleId,

        @NotNull(message = "금액은 필수입니다.")
        Long money,

        List<String> tags
) {
}
