package ac.dnd.mur.server.heart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateHeartRequest(
        @NotNull(message = "관계 정보는 필수입니다.")
        Long relationId,

        @NotNull(message = "주고 받은 마음 여부는 필수입니다.")
        Boolean give,

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
