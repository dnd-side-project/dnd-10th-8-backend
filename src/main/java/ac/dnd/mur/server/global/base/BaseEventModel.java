package ac.dnd.mur.server.global.base;

import java.time.LocalDateTime;

public interface BaseEventModel {
    default LocalDateTime eventPublishedAt() {
        return LocalDateTime.now();
    }
}
