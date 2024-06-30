package turing.turing.domain.notice.fcm;

import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.Homework;
import turing.turing.domain.homework.HomeworkRepository;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.fcm.dto.FcmSendDeviceDto;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import com.google.firebase.messaging.Notification;
import turing.turing.domain.noticeSetting.NoticeSettingRepository;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.teacher.Teacher;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FcmServiceImpl implements FcmService{

    private final FirebaseMessaging firebaseMessaging;
    private final NoticeRepository noticeRepository;
    private final HomeworkRepository homeworkRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;
    private final NoticeSettingRepository noticeSettingRepository;
    @Override
    public int sendMessageTo(FcmSendDto fcmSendDto){
        Message message = makeMessage(fcmSendDto);
        try {
            firebaseMessaging.send(message);
            return 1;
        }catch (FirebaseMessagingException e) {
            //에러처리
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }

    }

    @Override
    @Transactional(readOnly = true)
    //알림이 켜져있는지 확인해야함
    public List<FcmSendDeviceDto> selectFcmSendList() {
        List<FcmSendDeviceDto> fcmSendDeviceDtos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();


        //토큰 가져오기
        //알림장 작성 (수업이 끝나기 10분전)
        LocalDateTime targetDateTime = currentDateTime.plusMinutes(10).withSecond(0).withNano(0);
        LocalDate targetDate = targetDateTime.toLocalDate();
        LocalTime targetTime = targetDateTime.toLocalTime();

        List<Schedule> scheduleList = scheduleRepository.searchScheduleByDateAndTime(targetDate, targetTime);
        //for문을 통해 과외공간 ID 가져 온 다음 과외공간을 통해 선생님ID로 알림 전송
        for(Schedule s : scheduleList){
            Teacher teacher = s.getStudyRoom().getTeacher();
            Student student = s.getStudyRoom().getStudent();
            boolean isTurned = noticeSettingRepository.findByMemberIdAndRoleAndCategory(teacher.getId(), "TEACHER", "NOTEBOOK").getEnabled();
            if(isTurned) {
                FcmSendDeviceDto dto = FcmSendDeviceDto.builder()
                        .dvcTkn(teacher.getFcmToken())
                        .category("NOTEBOOK")
                        .senderName(student.getName())
                        .session(s.getSession())
                        .build();
                fcmSendDeviceDtos.add(dto);
            }
        }

        //숙제 알리미 (수업 하루전까지 숙제를 끝나지 못했을 때)
        //하루 추가
        LocalDate homeworkDate = currentDate.plusDays(1);
        LocalDateTime hwTargetDateTime = homeworkDate.atTime(currentDateTime.getHour(), currentDateTime.getMinute(), 0, 0);
        Timestamp hwTargetDate = Timestamp.from(hwTargetDateTime.toInstant(ZoneOffset.UTC));

        //하루 전인 알림장 가져오기
        System.out.println("Target Timestamp: " + hwTargetDate);
        List<Notebook> notebookList =noticeRepository.serachNoteBookByDate(hwTargetDate);
        //for문 돌리면 알림장 안에 숙제 중 안된게 있으면 fcmSendDeviceDto에 추가
        
        for (Notebook notebook : notebookList) {
            List<Homework> homeworkList = homeworkRepository.findAllByNotebook(notebook);
            for (Homework homework : homeworkList) {
                if (!homework.getIsDone()) {
//                    //알림보낼 학생 토큰 찾기

//                    Schedule schedule = scheduleRepository.findById(notebook.getSchedule().getId())
//                            .orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));
//                    StudyRoom studyRoom =schedule.getStudyRoom();
//                    Student student = studentRepository.findById(studyRoom.getStudent().getId())
//                            .orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));

                    Schedule schedule = notebook.getSchedule();
                    Teacher teacher = schedule.getStudyRoom().getTeacher();
                    Student student = schedule.getStudyRoom().getStudent();
                    boolean isTurned = noticeSettingRepository.findByMemberIdAndRoleAndCategory(teacher.getId(), "TEACHER", "NOTEBOOK").getEnabled();
                    if(isTurned) {
                        //토큰 담기
                        FcmSendDeviceDto dto = FcmSendDeviceDto.builder()
                                .dvcTkn(teacher.getFcmToken())
                                .senderName(student.getName())
                                .category("HOMEWORK")
                                .build();
                        fcmSendDeviceDtos.add(dto);
                        break;
                    }
                }

            }
        }

        return fcmSendDeviceDtos;
    }


    //시간별 푸시 알림
    //리포트 작성 - 끝날 때
   // 알림장 작성 - 수업 끝나기 10분전
    //숙제- 하루전
    private Message makeMessage(FcmSendDto fcmSendDto) {
       //메세지 만들 떄... 어떻게 만들지?

        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getBody())
                .build();

        Message message = Message.builder()
                .setToken(fcmSendDto.getToken())
                .setNotification(notification)
                .putData("category", fcmSendDto.getCategory())
                .build();

        return message;
    }

    public FcmSendDto test1() {
        FcmSendDto f = FcmSendDto.builder()
                .body("sd")
                .title("s")
                .token("sdds").build();
        return f;
    }

}
