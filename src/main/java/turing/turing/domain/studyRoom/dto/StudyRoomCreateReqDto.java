package turing.turing.domain.studyRoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.student.Student;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;
import turing.turing.domain.teacher.Teacher;

import java.time.LocalDate;
import java.util.List;

public record StudyRoomCreateReqDto(
        String studentName,
        String studentSchool,
        String studentYear,
        String subject,
        Integer baseSession,
        List<StudyTimeReqDto> studyTimes,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate
) {
        public Student toStudent(){
                return new Student(studentName, studentSchool, studentYear);
        }

        public StudyRoom toStudyRoom(Teacher teacher, Student student) {
                return new StudyRoom(subject, baseSession, teacher, student);
        }

}
