package ac.dnd.mur.server.heart.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

public record HeartHistory(
        long relationId,
        String relationName,
        long groupid,
        String groupName,
        long giveMoney,
        long takeMoney
) {
    @QueryProjection
    public HeartHistory {
    }
}
