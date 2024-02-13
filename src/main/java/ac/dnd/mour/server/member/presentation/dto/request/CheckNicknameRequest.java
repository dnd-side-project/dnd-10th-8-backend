package ac.dnd.mour.server.member.presentation.dto.request;

import ac.dnd.mour.server.member.domain.model.Nickname;
import jakarta.validation.constraints.NotBlank;

public record CheckNicknameRequest(
        @NotBlank(message = "중복 체크할 닉네임은 필수입니다.")
        String nickname
) {
    public Nickname toNickname() {
        return Nickname.from(nickname);
    }
}
