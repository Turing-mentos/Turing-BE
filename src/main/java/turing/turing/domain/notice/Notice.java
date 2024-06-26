package turing.turing.domain.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Size(max = 20)
    @NotNull
    @Column(name = "sender_role", nullable = false, length = 20)
    private String senderRole;

    @NotNull
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Size(max = 20)
    @NotNull
    @Column(name = "receiver_role", nullable = false, length = 20)
    private String receiverRole;

    @NotNull
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

    @Size(max = 200)
    @NotNull
    @Column(name = "message", nullable = false, length = 200)
    private String message;

}