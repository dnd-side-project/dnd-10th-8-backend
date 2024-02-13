package ac.dnd.mour.server.group.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateGroupRequest(
        @NotBlank(message = "그룹 이름은 필수입니다.")
        String name
) {
}
