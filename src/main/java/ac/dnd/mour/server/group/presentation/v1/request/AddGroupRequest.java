package ac.dnd.mour.server.group.presentation.v1.request;

import jakarta.validation.constraints.NotBlank;

public record AddGroupRequest(
        @NotBlank(message = "그룹 이름은 필수입니다.")
        String name
) {
}
