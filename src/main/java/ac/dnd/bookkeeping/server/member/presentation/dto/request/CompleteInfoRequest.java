package ac.dnd.bookkeeping.server.member.presentation.dto.request;

import ac.dnd.bookkeeping.server.member.domain.model.Member;
import ac.dnd.bookkeeping.server.member.domain.model.Nickname;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CompleteInfoRequest(
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

    public Member.Gender toGender() {
        return Member.Gender.from(gender);
    }
}
