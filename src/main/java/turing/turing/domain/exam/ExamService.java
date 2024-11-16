package turing.turing.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.exam.converter.ExamConverter;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.CreateExamResponse;
import turing.turing.domain.exam.dto.ExamDto;
import turing.turing.domain.member.Role;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.ExamErrorCode;
import turing.turing.global.exception.errorCode.StudyRoomErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamService {

    private final ExamRepository examRepository;
    private final StudyRoomRepository studyRoomRepository;

    public ExamDto getExamSchedule(Long examId) {
        Exam exam = examRepository.findExamWithStudyRoomById(examId)
                .orElseThrow(() -> new RestApiException(ExamErrorCode.EXAM_NOT_FOUND));

        return ExamConverter.toDto(exam);
    }

    @Transactional
    public CreateExamResponse createExamSchedules(CustomUserDetails customUserDetails, CreateExamRequest request) {
        StudyRoom studyRoom = studyRoomRepository.findWithStudentById(request.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(StudyRoomErrorCode.STUDY_ROOM_NOT_FOUND));

        Exam exam = ExamConverter.toEntity(request, studyRoom);
        Long examId = examRepository.save(exam).getId();

        CreateExamResponse response = setBaseField(customUserDetails, studyRoom);
        response.setExamId(examId);

        return response;
    }

    @Transactional
    public Long modifyExamSchedule(ExamDto request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new RestApiException(ExamErrorCode.EXAM_NOT_FOUND));

        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(StudyRoomErrorCode.STUDY_ROOM_NOT_FOUND));

        return exam.update(request, studyRoom);
    }

    @Transactional
    public void deleteExamSchedule(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RestApiException(ExamErrorCode.EXAM_NOT_FOUND));

        examRepository.delete(exam);
    }

    private CreateExamResponse setBaseField(CustomUserDetails customUserDetails, StudyRoom studyRoom) {
        Long receiverId;
        Role receiverRole;
        Role senderRole = customUserDetails.getRole();

        if (senderRole.equals(Role.STUDENT)) {
            receiverId = studyRoom.getTeacher().getId();
            receiverRole = Role.TEACHER;
        } else {
            receiverId = studyRoom.getStudent().getId();
            receiverRole = Role.STUDENT;
        }

        return CreateExamResponse.builder()
                .receiverId(receiverId)
                .receiverRole(receiverRole)
                .senderId(customUserDetails.getMemberId())
                .senderRole(senderRole)
                .build();
    }
}
