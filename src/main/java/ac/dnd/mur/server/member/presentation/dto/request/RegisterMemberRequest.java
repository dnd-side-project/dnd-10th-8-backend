package ac.dnd.mur.server.member.presentation.dto.request;

import ac.dnd.mur.server.member.domain.model.Email;
import ac.dnd.mur.server.member.domain.model.Gender;
import ac.dnd.mur.server.member.domain.model.Nickname;
import ac.dnd.mur.server.member.domain.model.SocialPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterMemberRequest(
        @NotBlank(message = "소셜 플랫폼 ID는 필수입니다.")
        String socialId,

        @NotBlank(message = "소셜 플랫폼 이메일은 필수입니다.")
        String email,

        @NotBlank(message = "소셜 플랫폼 프로필 이미지 URL은 필수입니다.")
        String profileImageUrl,

        @NotBlank(message = "소셜 플랫폼 사용자 이름은 필수입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "성별은 필수입니다.")
        String gender,

        @NotNull(message = "생년월일은 필수입니다.")
        LocalDate birth
) {
    public SocialPlatform toSocialPlatform() {
        return SocialPlatform.of(socialId, Email.from(email));
    }

    public Nickname toNickname() {
        return Nickname.from(nickname);
    }

    public Gender toGender() {
        return Gender.from(gender);
    }
}
