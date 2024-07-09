package turing.turing.domain.noticeSetting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.notice.Notice;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NoticeSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_setting_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled ;

    @Size(max = 20)
    @NotNull
    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @NotNull
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Size(max = 20)
    @NotNull
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    public NoticeSetting changeEnabled(Boolean enabled) {
        this.enabled = enabled;

        return this;
    }

}