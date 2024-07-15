package turing.turing.domain.code;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.studyRoom.StudyRoom;

@Getter
@Entity
@NoArgsConstructor
public class ConnectionCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_code_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

    public ConnectionCode(Integer code, StudyRoom studyRoom) {
        this.code = code;
        this.studyRoom = studyRoom;
    }
}
