package ac.dnd.mour.server.heart.presentation.v1.request;

import ac.dnd.mour.server.heart.application.usecase.query.GetHeartHistoryWithRelation;
import jakarta.validation.constraints.NotBlank;

public record GetHeartHistoriesWithRelationRequest(
        @NotBlank(message = "정렬 정보는 필수입니다.")
        String sort
) {
    public GetHeartHistoryWithRelation toQuery(
            final long memberId,
            final long relationid
    ) {
        return new GetHeartHistoryWithRelation(
                memberId,
                relationid,
                sort
        );
    }
}
