package turing.turing.domain.exam;


import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.CreateExamResponse;
import turing.turing.domain.exam.dto.ExamDto;
import turing.turing.domain.exam.dto.UpdateExamRequest;

public interface ExamService {

    ExamDto getExamSchedule(Long examId);

    CreateExamResponse createExamSchedules(CustomUserDetails customUserDetails, CreateExamRequest request);

    Long updateExamSchedule(UpdateExamRequest request);

    void deleteExamSchedule(Long examId);
}
