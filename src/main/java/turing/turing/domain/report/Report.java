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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.studyRoom.StudyRoom;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

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

    public Report updateField(int paragraphNum, String content) {
        switch (paragraphNum) {
            case 1:
                return Report.builder()
                        .id(this.id)
                        .opening(content)
                        .studyProgress(this.studyProgress)
                        .feedback(this.feedback)
                        .money(this.money)
                        .closing(this.closing)
                        .studyRoom(this.studyRoom)
                        .build();
            case 2:
                return Report.builder()
                        .id(this.id)
                        .opening(this.opening)
                        .studyProgress(content)
                        .feedback(this.feedback)
                        .money(this.money)
                        .closing(this.closing)
                        .studyRoom(this.studyRoom)
                        .build();
            case 3:
                return Report.builder()
                        .id(this.id)
                        .opening(this.opening)
                        .studyProgress(this.studyProgress)
                        .feedback(content)
                        .money(this.money)
                        .closing(this.closing)
                        .studyRoom(this.studyRoom)
                        .build();
            case 4:
                return Report.builder()
                        .id(this.id)
                        .opening(this.opening)
                        .studyProgress(this.studyProgress)
                        .feedback(this.feedback)
                        .money(content)
                        .closing(this.closing)
                        .studyRoom(this.studyRoom)
                        .build();
            case 5:
                return Report.builder()
                        .id(this.id)
                        .opening(this.opening)
                        .studyProgress(this.studyProgress)
                        .feedback(this.feedback)
                        .money(this.money)
                        .closing(content)
                        .studyRoom(this.studyRoom)
                        .build();
            default:
                //유효하지 않음
                throw new IllegalArgumentException("Invalid paragraph number: " + paragraphNum);
        }
    }
}