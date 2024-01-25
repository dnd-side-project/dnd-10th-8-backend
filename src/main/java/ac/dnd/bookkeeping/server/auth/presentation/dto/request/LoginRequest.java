package ac.dnd.bookkeeping.server.auth.presentation.dto.request;

import ac.dnd.bookkeeping.server.member.domain.model.Email;
import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.SocialPlatform;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public record LoginRequest(
        @NotBlank(message = "소셜 플랫폼 ID는 필수입니다.")
        String socialId,

        @NotBlank(message = "소셜 플랫폼 이메일은 필수입니다.")
        String email,

        @NotBlank(message = "소셜 플랫폼 사용자 이름은 필수입니다.")
        String name,

        String gender,

        LocalDate birth
) {
    public SocialPlatform toSocialPlatform() {
        return SocialPlatform.of(socialId, Email.from(email));
    }

    public Member.Gender toGender() {
        if (!StringUtils.hasText(gender)) {
            return null;
        }
        return Member.Gender.from(gender);
    }
}
