package turing.turing.domain.studyRoom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.teacher.Teacher;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @NotNull
    @Column(name = "link_status", nullable = false)
    private Boolean linkStatus = false;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @NotNull
    @Column(name = "base_session", nullable = false)
    private Integer baseSession;

    @Size(max = 50)
    @NotNull
    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

}