package ac.dnd.mur.server.group.presentation;

import ac.dnd.mur.server.auth.domain.model.Authenticated;
import ac.dnd.mur.server.global.annotation.Auth;
import ac.dnd.mur.server.global.dto.ResponseWrapper;
import ac.dnd.mur.server.group.application.usecase.AddGroupUseCase;
import ac.dnd.mur.server.group.application.usecase.GetMemberGroupUseCase;
import ac.dnd.mur.server.group.application.usecase.RemoveGroupUseCase;
import ac.dnd.mur.server.group.application.usecase.command.AddGroupCommand;
import ac.dnd.mur.server.group.application.usecase.command.RemoveGroupCommand;
import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.group.presentation.dto.request.AddGroupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "사용자별 그룹 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManageGroupApiController {
    private final AddGroupUseCase addGroupUseCase;
    private final RemoveGroupUseCase removeGroupUseCase;
    private final GetMemberGroupUseCase getMemberGroupUseCase;

    @Operation(summary = "그룹 추가 Endpoint")
    @PostMapping("/v1/groups")
    public ResponseEntity<ResponseWrapper<Long>> addGroup(
            @Auth final Authenticated authenticated,
            @RequestBody @Valid final AddGroupRequest request
    ) {
        final long groupId = addGroupUseCase.invoke(new AddGroupCommand(
                authenticated.id(),
                request.name()
        ));
        return ResponseEntity.ok(ResponseWrapper.from(groupId));
    }

    @Operation(summary = "그룹 삭제 Endpoint")
    @DeleteMapping("/v1/groups/{groupId}")
    public ResponseEntity<Void> removeGroup(
            @Auth final Authenticated authenticated,
            @PathVariable(name = "groupId") final Long groupId
    ) {
        removeGroupUseCase.invoke(new RemoveGroupCommand(
                authenticated.id(),
                groupId
        ));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자가 관리하고 있는 그룹 조회 Endpoint")
    @GetMapping("/v1/groups/me")
    public ResponseEntity<ResponseWrapper<List<GroupResponse>>> getMemberGroup(
            @Auth final Authenticated authenticated
    ) {
        final List<GroupResponse> result = getMemberGroupUseCase.invoke(authenticated.id());
        return ResponseEntity.ok(ResponseWrapper.from(result));
    }
}
