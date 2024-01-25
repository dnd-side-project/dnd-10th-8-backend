package ac.dnd.bookkeeping.server.auth.presentation.dto.request;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "소셜 플랫폼 ID는 필수입니다.")
        String socialId,

        @NotBlank(message = "소셜 플랫폼 이메일은 필수입니다.")
        String email,

        @NotBlank(message = "소셜 플랫폼 사용자 이름은 필수입니다.")
        String name
) {
    public SocialPlatform toSocialPlatform() {
        return SocialPlatform.of(socialId, Email.from(email));
    }
}
