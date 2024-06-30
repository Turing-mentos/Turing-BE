package turing.turing.domain.report;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.studyRoom.StudyRoom;

@Getter
@Entity
@NoArgsConstructor
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Size(max = 800)
    @Column(name = "opening", length = 800)
    private String opening;

    @Size(max = 800)
    @Column(name = "study_progress", length = 800)
    private String studyProgress;

    @Size(max = 800)
    @Column(name = "feedback", length = 800)
    private String feedback;

    @Size(max = 800)
    @Column(name = "money", length = 800)
    private String money;

    @Size(max = 800)
    @Column(name = "closing", length = 800)
    private String closing;

}