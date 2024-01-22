package ac.dnd.bookkeeping.server.global.base;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

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
