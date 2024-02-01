package ac.dnd.mur.server.global.base;

import java.time.LocalDateTime;

public abstract class BaseEventModel {
    private final LocalDateTime eventPublishedAt;

    protected BaseEventModel() {
        this.eventPublishedAt = LocalDateTime.now();
    }

    public LocalDateTime eventPublishedAt() {
        return eventPublishedAt;
    }
}
