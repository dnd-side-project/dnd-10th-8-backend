package ac.dnd.mur.server.global.base;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEventModel {
    protected final LocalDateTime eventPublishedAt;

    protected BaseEventModel(final LocalDateTime eventPublishedAt) {
        this.eventPublishedAt = eventPublishedAt;
    }
}
