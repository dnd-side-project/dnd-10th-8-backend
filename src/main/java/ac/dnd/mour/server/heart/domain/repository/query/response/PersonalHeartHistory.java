package ac.dnd.mour.server.heart.domain.repository.query.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record PersonalHeartHistory(
        String event,
        String relationName,
        String groupName,
        long money,
        LocalDate day,
        String memo
) {
    @QueryProjection
    public PersonalHeartHistory {
    }
}
