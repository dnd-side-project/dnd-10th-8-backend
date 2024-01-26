package ac.dnd.bookkeeping.server.auth.presentation;

import ac.dnd.bookkeeping.server.auth.application.usecase.LoginUseCase;
import ac.dnd.bookkeeping.server.auth.application.usecase.LogoutUseCase;
import ac.dnd.bookkeeping.server.auth.application.usecase.command.LoginCommand;
import ac.dnd.bookkeeping.server.auth.application.usecase.command.LogoutCommand;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthMember;
import ac.dnd.bookkeeping.server.auth.domain.model.Authenticated;
import ac.dnd.bookkeeping.server.auth.presentation.dto.request.LoginRequest;
import ac.dnd.bookkeeping.server.global.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인/로그아웃 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthApiController {
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;

    @Operation(summary = "로그인 Endpoint")
    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthMember> login(
            @RequestBody @Valid final LoginRequest request
    ) {
        final AuthMember response = loginUseCase.invoke(new LoginCommand(
                request.socialId(),
                request.toEmail()
        ));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃 EndPoint")
    @PostMapping("/v1/auth/logout")
    public ResponseEntity<Void> logout(
            @Auth final Authenticated authenticated
    ) {
        logoutUseCase.invoke(new LogoutCommand(authenticated.id()));
        return ResponseEntity.noContent().build();
    }
}
