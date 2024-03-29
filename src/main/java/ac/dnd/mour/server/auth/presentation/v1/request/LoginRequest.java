package ac.dnd.mour.server.auth.presentation.v1.request;

import ac.dnd.mour.server.member.domain.model.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "소셜 플랫폼 ID는 필수입니다.")
        String socialId,

        @NotBlank(message = "소셜 플랫폼 이메일은 필수입니다.")
        String email
) {
    public Email toEmail() {
        return Email.from(email);
    }
}
