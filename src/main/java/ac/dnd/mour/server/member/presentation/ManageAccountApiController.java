package ac.dnd.mour.server.member.presentation;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.member.application.usecase.DeleteAccountUseCase;
import ac.dnd.mour.server.member.application.usecase.UpdateMemberUseCase;
import ac.dnd.mour.server.member.application.usecase.command.UpdateMemberCommand;
import ac.dnd.mour.server.member.presentation.dto.request.UpdateMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 계정 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageAccountApiController {
    private final UpdateMemberUseCase updateMemberUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @Operation(summary = "마이페이지 정보 수정 Endpoint")
    @PatchMapping("/v1/members/me")
    public ResponseEntity<Void> update(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final UpdateMemberRequest request
    ) {
        updateMemberUseCase.invoke(new UpdateMemberCommand(
                authenticated.id(),
                request.profileImageUrl(),
                request.toNickname(),
                request.toGender(),
                request.birth()
        ));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 탙퇴 처리 Endpoint")
    @DeleteMapping("/v1/members/me")
    public ResponseEntity<Void> delete(
            @Auth final Authenticated authenticated
    ) {
        deleteAccountUseCase.invoke(authenticated.id());
        return ResponseEntity.noContent().build();
    }
}
