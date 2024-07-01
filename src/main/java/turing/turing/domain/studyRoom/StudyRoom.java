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
import turing.turing.domain.student.Student;
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
    @Column(name = "link_status", nullable = false)
    private Boolean linkStatus = false;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @NotNull
    @Column(name = "base_session", nullable = false)
    private Integer baseSession;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public StudyRoom(String subject, Integer baseSession, Teacher teacher, Student student) {
        this.subject = subject;
        this.baseSession = baseSession;
        this.teacher = teacher;
        this.student = student;
    }

    // 기존에는 선생님이 등록해놓은 (가입되지 않은) 학생과 연결되어 있지만
    // 가입한 학생과 연결 시에 student 참조를 변경해주고, linkStatus를 업데이트 해주어야 함
    public void connectStudent(Student student){
        this.student = student;
        this.linkStatus = true;
    }
}