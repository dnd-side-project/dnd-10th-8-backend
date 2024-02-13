package ac.dnd.mour.server.heart.application.usecase.query.response;

import ac.dnd.mour.server.group.domain.model.GroupResponse;
import ac.dnd.mour.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mour.server.relation.domain.model.response.RelationSummary;

import java.util.List;

public record HeartHistoryDetails(
        RelationSummary relation,
        List<Long> giveHistories,
        List<Long> takeHistories
) {
    public static HeartHistoryDetails from(final HeartHistory heartHistory) {
        return new HeartHistoryDetails(
                new RelationSummary(
                        heartHistory.relationId(),
                        heartHistory.relationName(),
                        new GroupResponse(heartHistory.groupid(), heartHistory.groupName())
                ),
                heartHistory.giveHistories(),
                heartHistory.takeHistories()
        );
    }
}
