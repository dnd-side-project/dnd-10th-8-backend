package ac.dnd.mur.server.relation.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRelationRequest(
        @NotNull(message = "그룹 정보는 필수입니다.")
        Long groupId,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "연락처는 필수입니다.")
        String phone,

        String memo
) {
}
