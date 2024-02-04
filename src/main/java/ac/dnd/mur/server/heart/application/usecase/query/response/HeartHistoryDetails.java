package ac.dnd.mur.server.heart.application.usecase.query.response;

import ac.dnd.mur.server.heart.domain.repository.query.response.HeartHistory;
import ac.dnd.mur.server.relation.domain.repository.query.response.RelationDetails;

public record HeartHistoryDetails(
        RelationDetails relation,
        long giveMoney,
        long takeMoney
) {
    public static HeartHistoryDetails from(final HeartHistory heartHistory) {
        return new HeartHistoryDetails(
                new RelationDetails(
                        heartHistory.relationId(),
                        heartHistory.relationName(),
                        heartHistory.groupid(),
                        heartHistory.groupName()
                ),
                heartHistory.giveMoney(),
                heartHistory.takeMoney()
        );
    }
}
