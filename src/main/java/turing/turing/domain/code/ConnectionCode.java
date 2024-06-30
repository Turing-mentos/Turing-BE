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
    @Column(name = "connection_code", nullable = false)
    private Integer connectionCode;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;
}
