package ac.dnd.mour.server.heart.presentation.v1.request;

import ac.dnd.mour.server.heart.application.usecase.query.GetHeartHistory;
import jakarta.validation.constraints.NotBlank;

public record GetHeartHistoriesRequest(
        @NotBlank(message = "정렬 정보는 필수입니다.")
        String sort,

        String name
) {
    public GetHeartHistory toQuery(final long memberId) {
        return new GetHeartHistory(
                memberId,
                sort,
                name
        );
    }
}
