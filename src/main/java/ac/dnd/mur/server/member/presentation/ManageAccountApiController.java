package ac.dnd.mur.server.member.presentation;

import ac.dnd.mur.server.auth.domain.model.AuthMember;
import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.member.application.usecase.DeleteAccountUseCase;
import ac.dnd.mur.server.member.application.usecase.ManageResourceUseCase;
import ac.dnd.mur.server.member.application.usecase.RegisterAccountUseCase;
import ac.dnd.mur.server.member.application.usecase.UpdateMemberUseCase;
import ac.dnd.mur.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.mur.server.member.application.usecase.command.UpdateMemberCommand;
import ac.dnd.mur.server.member.presentation.dto.request.CheckNicknameRequest;
import ac.dnd.mur.server.member.presentation.dto.request.RegisterMemberRequest;
import ac.dnd.mur.server.member.presentation.dto.request.UpdateMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 계정 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageAccountApiController {
    private final ManageResourceUseCase manageResourceUseCase;
    private final RegisterAccountUseCase registerAccountUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @Operation(summary = "닉네임 중복 체크 Endpoint")
    @GetMapping("/v1/members/check-nickname")
    public ResponseEntity<ResponseWrapper<Boolean>> checkNickname(
            @ModelAttribute @Valid final CheckNicknameRequest request
    ) {
        final boolean result = manageResourceUseCase.isUniqueNickname(request.toNickname());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "회원가입 + 로그인 처리 Endpoint")
    @PostMapping("/v1/members")
    public ResponseEntity<AuthMember> register(
            @RequestBody @Valid final RegisterMemberRequest request
    ) {
        final AuthMember response = registerAccountUseCase.invoke(new RegisterMemberCommand(
                request.toSocialPlatform(),
                request.profileImageUrl(),
                request.name(),
                request.toNickname(),
                request.toGender(),
                request.birth()
        ));
        return ResponseEntity.ok(response);
    }

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
