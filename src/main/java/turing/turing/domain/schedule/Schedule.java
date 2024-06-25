package turing.turing.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Size(max = 1)
    @NotNull
    @Column(name = "day", nullable = false, length = 1)
    private String day;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Size(max = 100)
    @NotNull
    @Column(name = "student_name", nullable = false, length = 100)
    private String studentName;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @NotNull
    @Column(name = "session", nullable = false)
    private Integer session;

}