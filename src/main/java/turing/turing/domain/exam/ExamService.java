package turing.turing.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.exam.converter.ExamConverter;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.ExamDto;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {

    private final ExamRepository examRepository;
    private final StudyRoomRepository studyRoomRepository;

    @Transactional(readOnly = true)
    public ExamDto getExamSchedule(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        return ExamConverter.toDto(exam);
    }

    public Long createExamSchedules(CreateExamRequest request) {
        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Exam exam = ExamConverter.toEntity(request, studyRoom);

        return examRepository.save(exam).getId();
    }

    public Long modifyExamSchedule(ExamDto request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        return exam.update(request, studyRoom);
    }

    public void deleteExamSchedule(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        examRepository.delete(exam);
    }

}
