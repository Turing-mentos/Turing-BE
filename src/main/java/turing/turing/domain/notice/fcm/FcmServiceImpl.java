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
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.student.StudentRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;


import java.time.LocalDate;
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
    public List<FcmSendDeviceDto> selectFcmSendList() {
        List<FcmSendDeviceDto> fcmSendDeviceDtos = new ArrayList<>();

        //토큰 가져오기
      //  뭘.....가져오기
        //알림장 작성 (수업이 끝나기 10분전)
        List<Notebook> notebookLis1t = null;


        //숙제 알리미 (수업 하루전까지 숙제를 끝나지 못했을 때)
        LocalDate targetDate = LocalDate.now().plusDays(1);
        List<Notebook> notebookList =noticeRepository.findNoteBookByDate(targetDate);

        for (Notebook notebook : notebookList) {
            List<Homework> homeworkList = homeworkRepository.findAllByNotebook(notebook);
            for (Homework homework : homeworkList) {
//                if (!homework.isDone()) {
//                    Schedule schedule = scheduleRepository.findById(notebook.getSchedule().getId()).orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));
//                    StudyRoom studyRoom =schedule.getStudyRoom;
//                    Student student = studentRepository.findById(studyRoom.getStudent.getId());
//                    FcmSendDeviceDto dto = FcmSendDeviceDto.builder()
//                            .dvcTkn(student.getFcmToken())
//                            .category("HOMEWORK")
//                            .build();
//                    fcmSendDeviceDtos.add(dto);
//                    break;
//                }
            }
        }

        //기준 회차 수업이 끝났을 때
        List<Schedule> scheduleList = null;
        LocalDate targetDate1 = LocalDate.now();
       // List<Schedule> scheduleList1 =  scheduleRepository.findAllBySession();
        List<FcmSendDeviceDto> s = new ArrayList<>();
        //FcmSendDeviceDto d = new FcmSendDeviceDto("");
        //s.add();

        return s;
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
                .setImage(null)
                .build();

        Message message = Message.builder()
                .setToken(fcmSendDto.getToken())
                .setNotification(notification)
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
