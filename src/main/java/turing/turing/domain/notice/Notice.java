package turing.turing.domain.notice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.member.Role;



@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_role", nullable = false, length = 20)
    private Role senderRole;

    @NotNull
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_role", nullable = false, length = 20)
    private Role receiverRole;

    @NotNull
    @Builder.Default
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

    @Size(max = 150)
    @NotNull
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 200)
    @NotNull
    @Column(name = "body", nullable = false, length = 200)
    private String body;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Size(max = 20)
    @NotNull
    @Column(name = "category", nullable = false, length = 20)
    private String category;

    public Notice updateRead(Boolean readStatus) {
        this.readStatus = readStatus;

        return this;
    }
}