package turing.turing.domain.exam.converter;

import turing.turing.domain.exam.Exam;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.ExamDto;
import turing.turing.domain.studyRoom.StudyRoom;

public class ExamConverter {

    public static Exam toEntity(CreateExamRequest request, StudyRoom studyRoom) {
        return Exam.builder()
                .examName(request.getExamName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .studentName(studyRoom.getStudentName())
                .studyRoom(studyRoom)
                .build();
    }

    public static ExamDto toDto(Exam exam) {
        return ExamDto.builder()
                .examId(exam.getId())
                .examName(exam.getExamName())
                .startDate(exam.getStartDate())
                .endDate(exam.getEndDate())
                .studyRoomId(exam.getStudyRoom().getId())
                .build();
    }
}
