package ac.dnd.mour.server.heart.application.usecase.query.response;

import ac.dnd.mour.server.heart.domain.model.Heart;
import ac.dnd.mour.server.heart.domain.model.Tag;

import java.time.LocalDate;
import java.util.List;

public record SpecificRelationHeartHistoryDetails(
        long id,
        boolean give,
        long money,
        LocalDate day,
        String event,
        String memo,
        List<String> tags
) {
    public static SpecificRelationHeartHistoryDetails from(final Heart heart) {
        return new SpecificRelationHeartHistoryDetails(
                heart.getId(),
                heart.isGive(),
                heart.getMoney(),
                heart.getDay(),
                heart.getEvent(),
                heart.getMemo(),
                heart.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList()
        );
    }
}
