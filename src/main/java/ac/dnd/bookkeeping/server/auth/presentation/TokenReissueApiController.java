package ac.dnd.bookkeeping.server.auth.presentation;

import ac.dnd.bookkeeping.server.auth.application.usecase.ReissueTokenUseCase;
import ac.dnd.bookkeeping.server.auth.application.usecase.command.ReissueTokenCommand;
import ac.dnd.bookkeeping.server.auth.domain.model.AuthToken;
import ac.dnd.bookkeeping.server.global.annotation.ExtractToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ac.dnd.bookkeeping.server.auth.domain.model.TokenType.REFRESH;

@Tag(name = "토큰 재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenReissueApiController {
    private final ReissueTokenUseCase reissueTokenUseCase;

    @Operation(summary = "RefreshToken을 통한 토큰 재발급 Endpoint")
    @PostMapping("/v1/token/reissue")
    public ResponseEntity<AuthToken> reissueToken(
            @ExtractToken(tokenType = REFRESH) final String refreshToken
    ) {
        final AuthToken result = reissueTokenUseCase.invoke(new ReissueTokenCommand(refreshToken));
        return ResponseEntity.ok(result);
    }
}
