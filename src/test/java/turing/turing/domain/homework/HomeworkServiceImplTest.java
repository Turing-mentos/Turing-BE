package turing.turing.domain.homework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static turing.turing.global.exception.errorCode.HomeworkErrorCode.HOMEWORK_NOT_FOUND;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import turing.turing.domain.IntegrationTestSupport;
import turing.turing.domain.homework.dto.CreateHomeworkRequest;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.homework.dto.UpdateHomeworkRequest;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notebook.NotebookRepository;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;

class HomeworkServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private HomeworkRepository homeworkRepository;
    private Notebook notebook;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름", "학생성");
        teacherRepository.save(teacher);
        studentRepository.save(student);
        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        studyRoomRepository.save(studyRoom);
        Schedule schedule = new Schedule(LocalDate.now(), LocalTime.now(), LocalTime.now(), "학생이름", "과목", 8, studyRoom);
        scheduleRepository.save(schedule);
        notebook = new Notebook(schedule, Timestamp.valueOf("2025-12-25 18:00:00"));
        notebookRepository.save(notebook);
    }

    @DisplayName("숙제Id를 통해 숙제 정보를 조회할 수 있다.")
    @Test
    void getHomework() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();

        Long savedId = homeworkRepository.save(homework).getId();

        // when
        DetailedHomeworkDto response = homeworkService.getHomework(savedId);
        // then
        assertThat(response)
                .extracting("homeworkId", "category", "title", "rangeType", "rangeStart",
                        "rangeEnd", "content", "memo")
                .contains(savedId, "카테고리", "제목", "범위타입", 1, 10, "내용", "메모");
    }

    @DisplayName("숙제를 생성할 수 있다.")
    @Test
    void createHomework() {
        // given
        CreateHomeworkRequest request = new CreateHomeworkRequest("카테고리", "제목",
                "범위타입", 1, 10, "내용", "메모", notebook.getId());
        // when
        Long savedId = homeworkService.createHomework(request);

        // then
        assertThat(homeworkRepository.findById(savedId).get())
                .extracting("id", "category", "title", "rangeType", "rangeStart", "rangeEnd",
                        "content", "memo", "isDone")
                .contains(1L, "카테고리", "제목",
                        "범위타입", 1, 10, "내용", "메모", false);
    }

    @DisplayName("요청 정보로 숙제를 수정할 수 있다.")
    @Test
    void updateHomework() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();
        Long savedId = homeworkRepository.save(homework).getId();

        UpdateHomeworkRequest request = UpdateHomeworkRequest.builder()
                .homeworkId(savedId)
                .category("새로운 카테고리")
                .title("새로운 제목")
                .rangeType("새로운 범위타입")
                .rangeStart(11)
                .rangeEnd(20)
                .content("새로운 내용")
                .memo("새로운 메모")
                .build();
        // when
        homeworkService.updateHomework(request);

        // then
        assertThat(homeworkRepository.findById(savedId).get())
                .extracting("id", "category", "title", "rangeType", "rangeStart", "rangeEnd",
                        "content", "memo", "isDone")
                .contains(1L, "새로운 카테고리", "새로운 제목",
                        "새로운 범위타입", 11, 20, "새로운 내용", "새로운 메모", false);
    }

    @DisplayName("숙제 완료여부를 완료로 수정할 수 있다.")
    @Test
    void updateDone() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();

        Long savedId = homeworkRepository.save(homework).getId();
        // when
        homeworkService.updateDone(savedId);

        // then
        assertThat(homeworkRepository.findById(savedId).get().getIsDone()).isTrue();
    }

    @DisplayName("숙제 완료여부를 미완료로 수정할 수 있다.")
    @Test
    void updateNotDone() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();

        Long savedId = homeworkRepository.save(homework).getId();
        // when
        homeworkService.updateDone(savedId);
        homeworkService.updateDone(savedId);
        // then
        assertThat(homeworkRepository.findById(savedId).get().getIsDone()).isFalse();
    }

    @DisplayName("숙제를 삭제할 수 있다.")
    @Test
    void deleteHomework() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();
        Long savedId = homeworkRepository.save(homework).getId();

        // when
        homeworkService.deleteHomework(savedId);

        // then
        Optional<Homework> deletedHomework = homeworkRepository.findById(savedId);
        assertThat(deletedHomework).isEmpty();
    }

    @DisplayName("숙제Id를 통해 숙제 엔티티를 조회할 수 있다.")
    @Test
    void findById() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();
        Long savedId = homeworkRepository.save(homework).getId();

        // when // then
        assertThat(homework).isEqualTo(homeworkRepository.findById(savedId).get());
    }

    @DisplayName("존재하지 않는 Id로 조회할 경우 에러가 발생한다.")
    @Test
    void findByIdWithNotExistId() {
        // given
        Homework homework = Homework.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .memo("메모")
                .notebook(notebook)
                .build();
        Long savedId = homeworkRepository.save(homework).getId();

        // when // then
        assertThatThrownBy(() -> homeworkService.getHomework(savedId + 1))
                .isInstanceOf(RestApiException.class)
                .extracting("ErrorCode").isEqualTo(HOMEWORK_NOT_FOUND);
    }
}