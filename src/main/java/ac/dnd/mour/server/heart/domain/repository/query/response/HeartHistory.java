package ac.dnd.mour.server.heart.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record HeartHistory(
        long relationId,
        String relationName,
        long groupid,
        String groupName,
        List<Long> giveHistories,
        List<Long> takeHistories
) {
    public record RelationInfo(
            long relationId,
            String relationName,
            long groupid,
            String groupName
    ) {
        @QueryProjection
        public RelationInfo {
        }
    }

    public record MoneySummary(
            long relationId,
            long money
    ) {
        @QueryProjection
        public MoneySummary {
        }
    }
}
