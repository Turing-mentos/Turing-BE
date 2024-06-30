package turing.turing.domain.studyRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.dto.StudyRoomReqDto;
import turing.turing.domain.studyTime.StudyTime;
import turing.turing.domain.studyTime.StudyTimeRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudyTimeRepository studyTimeRepository;

    @Transactional
    public Long createStudyRoom(Long teacherId, StudyRoomReqDto studyRoomReqDto) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Student student = studyRoomReqDto.toStudent();
        studentRepository.save(student);

        StudyRoom studyRoom = studyRoomReqDto.toStudyRoom(teacher, student);
        studyRoomRepository.save(studyRoom);

        List<StudyTime> studyTimes = studyRoomReqDto.studyTimes().stream().map(studyTimeReqDto -> studyTimeReqDto.toEntity(studyRoom)).toList();
        studyTimeRepository.saveAll(studyTimes);

        /*
        기준 회차 (스케줄) 생성 필요!
         */

        return studyRoom.getId();
    }
}
