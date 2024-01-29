package ac.dnd.mur.server.global.base;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@MappedSuperclass
public abstract class BaseEntity<T> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @PrePersist
    void prePersist() {
        final LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        lastModifiedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }

    @SuppressWarnings("unchecked")
    @VisibleForTesting
    public T apply(final long id) {
        this.id = id;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @VisibleForTesting
    public T apply(final long id, final LocalDateTime now) {
        this.id = id;
        this.createdAt = now;
        this.lastModifiedAt = now;
        return (T) this;
    }
}
