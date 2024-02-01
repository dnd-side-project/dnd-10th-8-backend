package ac.dnd.mur.server.member.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.member.application.usecase.GetPrivateProfileUseCase;
import ac.dnd.mur.server.member.application.usecase.query.response.MemberPrivateProfile;
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
@RequestMapping("/api")
public class MemberProfileQueryApiController {
    private final GetPrivateProfileUseCase getPrivateProfileUseCase;

    @Operation(summary = "닉네임 중복 체크 Endpoint")
    @GetMapping("/v1/members/me")
    public ResponseEntity<MemberPrivateProfile> getPrivateProfile(
            @Auth final Authenticated authenticated
    ) {
        final MemberPrivateProfile result = getPrivateProfileUseCase.invoke(authenticated.id());
        return ResponseEntity.ok(result);
    }
}
