package turing.turing.domain.studyRoom.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import turing.turing.domain.student.Student;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;
import turing.turing.domain.teacher.Teacher;

import java.time.LocalDate;
import java.util.List;

public record StudyRoomCreateReqDto(
        String studentFirstName,
        String studentLastName,
        String studentSchool,
        String studentYear,
        String subject,
        Integer baseSession,
        Integer wage,
        @NotEmpty List<StudyTimeReqDto> studyTimes,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate
) {
        public Student toStudent(){
                return new Student(studentFirstName, studentLastName, studentSchool, studentYear, null, null);
        }

        public StudyRoom toStudyRoom(Teacher teacher, Student student) {
                return new StudyRoom(subject, baseSession, wage, teacher, student);
        }

}
