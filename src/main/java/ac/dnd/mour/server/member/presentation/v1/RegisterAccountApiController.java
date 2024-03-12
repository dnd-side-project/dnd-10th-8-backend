package ac.dnd.mour.server.member.presentation.v1;

import ac.dnd.mour.server.auth.domain.model.AuthMember;
import ac.dnd.mour.server.global.dto.ResponseWrapper;
import ac.dnd.mour.server.member.application.usecase.ManageResourceUseCase;
import ac.dnd.mour.server.member.application.usecase.RegisterAccountUseCase;
import ac.dnd.mour.server.member.application.usecase.command.RegisterMemberCommand;
import ac.dnd.mour.server.member.presentation.v1.request.CheckNicknameRequest;
import ac.dnd.mour.server.member.presentation.v1.request.RegisterMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입 플로우 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegisterAccountApiController {
    private final ManageResourceUseCase manageResourceUseCase;
    private final RegisterAccountUseCase registerAccountUseCase;

    @Operation(summary = "닉네임 중복 체크 Endpoint")
    @GetMapping("/members/check-nickname")
    public ResponseEntity<ResponseWrapper<Boolean>> checkNickname(
            @ModelAttribute @Valid final CheckNicknameRequest request
    ) {
        final boolean result = manageResourceUseCase.isUniqueNickname(request.toNickname());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }

    @Operation(summary = "회원가입 + 로그인 처리 Endpoint")
    @PostMapping("/members")
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
}
