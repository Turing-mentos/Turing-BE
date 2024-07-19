package turing.turing.domain.studyRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.code.ConnectionCode;
import turing.turing.domain.code.ConnectionCodeRepository;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.dto.DetailedStudyRoomResDto;
import turing.turing.domain.studyRoom.dto.StudyRoomCreateReqDto;
import turing.turing.domain.studyRoom.dto.StudyRoomResDto;
import turing.turing.domain.studyRoom.dto.StudyRoomUpdateReqDto;
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
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long createStudyRoom(Long teacherId, StudyRoomCreateReqDto studyRoomCreateReqDto) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Student student = studyRoomCreateReqDto.toStudent();
        studentRepository.save(student);

        StudyRoom studyRoom = studyRoomCreateReqDto.toStudyRoom(teacher, student);
        studyRoomRepository.save(studyRoom);

        List<StudyTime> studyTimes = studyRoomCreateReqDto.studyTimes().stream().map(studyTimeReqDto -> studyTimeReqDto.toEntity(studyRoom)).toList();
        studyTimeRepository.saveAll(studyTimes);

        /*
        기준 회차 (스케줄) 생성 필요!
         */

        return studyRoom.getId();
    }

    @Transactional
    public void updateStudyRoom(Long studyRoomId, StudyRoomUpdateReqDto studyRoomUpdateReqDto) {

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        studyRoom.updateStudyRoom(studyRoomUpdateReqDto.subject(), studyRoomUpdateReqDto.baseSession());

        // StudyTime의 경우, 요일이 추가/삭제될 수도 있기 때문에 변경 감지가 아닌 기존 StudyTime 삭제 후 다시 생성하도록 함
        studyTimeRepository.deleteByStudyRoomId(studyRoomId);  // 벌크 연산을 통해 삭제
        List<StudyTime> studyTimes = studyRoomUpdateReqDto.studyTimes().stream().map(studyTimeReqDto -> studyTimeReqDto.toEntity(studyRoom)).toList();
        studyTimeRepository.saveAll(studyTimes);
    }

    @Transactional
    public void deleteStudyRoom(Long studyRoomId){

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        studyRoomRepository.delete(studyRoom);
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

        ConnectionCode connectionCode = connectionCodeRepository.findWithStudyRoomAndStudentByCode(code)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 기존 학생 가져오기
        Student nonSignUpStudent = connectionCode.getStudyRoom().getStudent();

        // 실제로 가입한 학생과 연결 (참조를 변경)
        connectionCode.getStudyRoom().connectStudent(student);

        // 기존 학생은 삭제
        studentRepository.delete(nonSignUpStudent);

        // 연결 후, 연결 코드는 삭제됨
        connectionCodeRepository.delete(connectionCode);
    }

    @Transactional
    public void disconnectTeacherStudent(Long studyRoomId){

        StudyRoom studyRoom = studyRoomRepository.findWithStudentById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 기존 학생의 정보를 통해 nonSignedUpStudent 생성
        Student nonSignUpStudent = new Student(
                studyRoom.getStudent().getFirstName(),
                studyRoom.getStudent().getLastName(),
                studyRoom.getStudent().getSchool(),
                studyRoom.getStudent().getYear(),
                studyRoom.getStudent().getPhone(),
                studyRoom.getStudent().getParentPhone()
        );
        studentRepository.save(nonSignUpStudent);
        studyRoom.disconnectStudent(nonSignUpStudent);
    }

    public List<StudyRoomResDto> getStudyRooms(Long memberId){

        // role == teacher
        Teacher teacher = teacherRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        List<StudyRoom> studyRoomList = studyRoomRepository.findAllWithStudentByTeacher(teacher);

        List<StudyRoomResDto> studyRoomResDtoList = studyRoomList.stream().map(StudyRoomResDto::of).toList();
        return studyRoomResDtoList;
    }

    public DetailedStudyRoomResDto getDetailedStudyRooms(Long studyRoomId){

        // role == teacher
        StudyRoom studyRoom = studyRoomRepository.findWithAllStudyTimeAndStudentById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        List<Schedule> scheduleList = scheduleRepository.findAllByStudyRoomOrderByDate(studyRoom);

        DetailedStudyRoomResDto detailedStudyRoomResDto = DetailedStudyRoomResDto.of(studyRoom, scheduleList);
        return detailedStudyRoomResDto;
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
