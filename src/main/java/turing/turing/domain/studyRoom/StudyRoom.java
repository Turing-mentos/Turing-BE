package turing.turing.domain.studyRoom;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.BaseEntity;
import turing.turing.domain.code.ConnectionCode;
import turing.turing.domain.exam.Exam;
import turing.turing.domain.question.Question;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.student.Student;
import turing.turing.domain.studyTime.StudyTime;
import turing.turing.domain.teacher.Teacher;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_id", nullable = false)
    private Long id;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
  
    @NotNull
    @Column(name = "wage", nullable = false)
    private Integer wage;
  
    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.REMOVE)
    private List<StudyTime> studyTimes = new ArrayList<>();

    @OneToOne(mappedBy = "studyRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private ConnectionCode connectionCode;

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.REMOVE)
    private List<Exam> exams = new ArrayList<>();


    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

    public StudyRoom(String subject, Integer baseSession, Integer wage, Teacher teacher, Student student) {
        this.subject = subject;
        this.baseSession = baseSession;
        this.wage = wage;
        this.teacher = teacher;
        this.student = student;
    }

    // 기존에는 선생님이 등록해놓은 (가입되지 않은) 학생과 연결되어 있지만
    // 가입한 학생과 연결 시에 student 참조를 변경해주고, linkStatus를 업데이트 해주어야 함
    public void connectStudent(Student student){
        this.student = student;
        this.linkStatus = true;
    }

    // 다시 nonSignUpStudent와 연결하고 linkStatus를 업데이트함
    public void disconnectStudent(Student nonSignUpStudent){
        this.student = nonSignUpStudent;
        this.linkStatus = false;
    }

    public void updateStudyRoom(String subject, Integer baseSession, Integer wage){
        this.subject = subject;
        this.baseSession = baseSession;
        this.wage = wage;
    }
}