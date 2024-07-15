package turing.turing.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

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

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 10)
    protected Provider provider;

    @Size(max = 300)
    @Column(name = "fcm_token", length = 300)
    protected String fcmToken;

}