package turing.turing.domain.studyRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.code.ConnectionCode;
import turing.turing.domain.code.ConnectionCodeRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.dto.StudyRoomReqDto;
import turing.turing.domain.studyRoom.dto.StudyRoomResDto;
import turing.turing.domain.studyTime.StudyTime;
import turing.turing.domain.studyTime.StudyTimeRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final ConnectionCodeRepository connectionCodeRepository;

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

    @Transactional
    public Integer getConnectionCode(Long studyRoomId){

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 이미 연결된 경우에는 코드를 생성하지 않음
        if(studyRoom.getLinkStatus())
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);

        // 이미 연결 코드가 존재한다면 그대로 리턴, 존재하지 않는다면 중복되지 않는 연결 코드 생성하여 리턴
        return connectionCodeRepository.findByStudyRoom(studyRoom)
                .map(ConnectionCode::getCode)
                .orElseGet(() -> {
                    ConnectionCode connectionCode = new ConnectionCode(generateCode(), studyRoom);
                    connectionCodeRepository.save(connectionCode);
                    return connectionCode.getCode();
                });
    }

    @Transactional
    public void connectTeacherStudent(Long studentId, Integer code){

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        ConnectionCode connectionCode = connectionCodeRepository.findWithStudyRoomByCode(code)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 실제로 가입한 학생과 연결 (참조를 변경)
        connectionCode.getStudyRoom().connectStudent(student);

        // 연결 후, 연결 코드는 삭제됨
        connectionCodeRepository.delete(connectionCode);
    }

    public List<StudyRoomResDto> getStudyRooms(Long memberId){

        // role == teacher
        Teacher teacher = teacherRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        List<StudyRoom> studyRoomList = studyRoomRepository.findAllWithStudentByTeacher(teacher);

        List<StudyRoomResDto> studyRoomResDtoList = studyRoomList.stream().map(StudyRoomResDto::of).toList();
        return studyRoomResDtoList;
    }

    // 중복되지 않는 6자리 연결 코드를 생성함
    public Integer generateCode(){
        while (true){
            Integer generatedCode = ThreadLocalRandom.current().nextInt(100000, 1000000);  // 6자리 코드 생성

            if(!connectionCodeRepository.existsByCode(generatedCode))   // 중복되지 않는 코드인지 검증
                return generatedCode;
        }
    }
}
