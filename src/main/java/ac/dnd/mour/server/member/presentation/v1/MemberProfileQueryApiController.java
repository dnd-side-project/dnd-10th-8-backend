package ac.dnd.mour.server.member.presentation.v1;

import ac.dnd.mour.server.auth.domain.model.Authenticated;
import ac.dnd.mour.server.global.annotation.Auth;
import ac.dnd.mour.server.member.application.usecase.GetPrivateProfileUseCase;
import ac.dnd.mour.server.member.application.usecase.query.response.MemberPrivateProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 프로필 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberProfileQueryApiController {
    private final GetPrivateProfileUseCase getPrivateProfileUseCase;

    @Operation(summary = "닉네임 중복 체크 Endpoint")
    @GetMapping("/members/me")
    public ResponseEntity<MemberPrivateProfile> getPrivateProfile(
            @Auth final Authenticated authenticated
    ) {
        final MemberPrivateProfile result = getPrivateProfileUseCase.invoke(authenticated.id());
        return ResponseEntity.ok(result);
    }
}
