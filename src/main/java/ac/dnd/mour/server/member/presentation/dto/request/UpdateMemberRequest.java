package ac.dnd.mour.server.member.presentation.dto.request;

import ac.dnd.mour.server.member.domain.model.Gender;
import ac.dnd.mour.server.member.domain.model.Nickname;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateMemberRequest(
        @NotBlank(message = "프로필 이미지 URL은 필수입니다.")
        String profileImageUrl,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "성별은 필수입니다.")
        String gender,

        @NotNull(message = "생년월일은 필수입니다.")
        LocalDate birth
) {
    public Nickname toNickname() {
        return Nickname.from(nickname);
    }

    public Gender toGender() {
        return Gender.from(gender);
    }
}
