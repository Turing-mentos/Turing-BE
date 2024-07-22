package turing.turing.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.BaseEntity;

@Getter
@MappedSuperclass
public abstract class Member extends BaseEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "email", length = 100)
    protected String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role", nullable = false, length = 10)
    protected Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "provider", length = 10)
    protected Provider provider;

    @Size(max = 300)
    @Column(name = "fcm_token", length = 300)
    protected String fcmToken;

    protected Member() {
    }

    public Member(Role role, String email, Provider provider, String fcmToken) {
        this.role = role;
        this.email = email;
        this.provider = provider;
        this.fcmToken = fcmToken;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}