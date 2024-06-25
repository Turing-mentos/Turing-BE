package turing.turing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    protected Status status;

    @CreatedDate
    @NotNull
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    protected Timestamp createdAt;

    @LastModifiedDate
    @NotNull
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    protected Timestamp updatedAt;

    protected BaseEntity() {
        this.status = Status.ACTIVE;
    }
}