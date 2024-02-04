package ac.dnd.mur.server.heart.application.usecase.query.response;

import ac.dnd.mur.server.group.domain.model.GroupResponse;
import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.relation.domain.model.response.RelationSummary;

public record HeartHistoryDetails(
        RelationSummary relation,
        long giveMoney,
        long takeMoney
) {
    public static HeartHistoryDetails from(final HeartHistory heartHistory) {
        return new HeartHistoryDetails(
                new RelationSummary(
                        heartHistory.relationId(),
                        heartHistory.relationName(),
                        new GroupResponse(heartHistory.groupid(), heartHistory.groupName())
                ),
                heartHistory.giveMoney(),
                heartHistory.takeMoney()
        );
    }
}
