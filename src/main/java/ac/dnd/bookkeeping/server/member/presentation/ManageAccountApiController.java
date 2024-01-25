package ac.dnd.bookkeeping.server.member.presentation;

import ac.dnd.bookkeeping.server.auth.domain.model.Authenticated;
import ac.dnd.bookkeeping.server.global.annotation.Auth;
import ac.dnd.bookkeeping.server.global.dto.ResponseWrapper;
import ac.dnd.bookkeeping.server.member.application.usecase.DeleteAccountUseCase;
import ac.dnd.bookkeeping.server.member.application.usecase.ManageResourceUseCase;
import ac.dnd.bookkeeping.server.member.application.usecase.command.CompleteInfoCommand;
import ac.dnd.bookkeeping.server.member.presentation.dto.request.CheckNicknameRequest;
import ac.dnd.bookkeeping.server.member.presentation.dto.request.CompleteInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 계정 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageAccountApiController {
    private final ManageResourceUseCase manageResourceUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @Operation(summary = "닉네임 중복 체크 Endpoint")
    @GetMapping("/v1/check-nickname")
    public ResponseEntity<ResponseWrapper<Boolean>> checkNickname(
            @ModelAttribute @Valid final CheckNicknameRequest request
    ) {
        final boolean result = manageResourceUseCase.isUniqueNickname(request.toNickname());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "온보딩 사용자 추가 정보 기입 Endpoint")
    @PatchMapping("/v1/members/me/complete")
    public ResponseEntity<Void> complete(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final CompleteInfoRequest request
    ) {
        manageResourceUseCase.completeInfo(new CompleteInfoCommand(
                authenticated.id(),
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
