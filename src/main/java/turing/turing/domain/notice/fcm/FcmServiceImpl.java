package turing.turing.domain.notice.fcm;

import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.Homework;
import turing.turing.domain.homework.HomeworkRepository;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notebook.NotebookRepository;
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
    private final NotebookRepository notebookRepository;
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
        LocalDateTime currentDateTime = LocalDateTime.now();

        addNotebookNotifications(fcmSendDeviceDtos, currentDateTime);
        addHomeworkNotifications(fcmSendDeviceDtos, currentDateTime);
        addSessionEndNotifications(fcmSendDeviceDtos);

        return fcmSendDeviceDtos;

    }
    //현재 시간과 일치하는 schedule 속에서 회차가 base회차랑 일치하는 것 찾기
    private void addSessionEndNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Schedule> scheduleList = scheduleRepository.findByDateAndEndTime(currentDate, currentTime);

        for (Schedule schedule : scheduleList) {
            if (schedule.getStudyRoom().getBaseSession() == schedule.getSession()) {
                Teacher teacher = schedule.getStudyRoom().getTeacher();
                Student student = schedule.getStudyRoom().getStudent();

                if (isNotificationEnabled(teacher.getId(), "TEACHER", "REPORT")) {
                    fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher.getFcmToken(), student.getName(), "REPORT", schedule.getSession(), 0L));
                }
            }
        }
    }
    private FcmSendDeviceDto buildFcmSendDeviceDto(String fcmToken, String senderName, String category, int session, Long targetId) {
        return FcmSendDeviceDto.builder()
                .dvcTkn(fcmToken)
                .senderName(senderName)
                .category(category)
                .session(session)
                .targetId(targetId)
                .build();
    }
    //알림장 수업 끝나기 10분전에 알랴주기
    private void addNotebookNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        LocalDateTime targetDateTime = currentDateTime.plusMinutes(10).withSecond(0).withNano(0);
        LocalDate targetDate = targetDateTime.toLocalDate();
        LocalTime targetTime = targetDateTime.toLocalTime();

        List<Schedule> scheduleList = scheduleRepository.searchScheduleByDateAndTime(targetDate, targetTime.getHour(), targetTime.getMinute());

        for (Schedule schedule : scheduleList) {
            Teacher teacher = schedule.getStudyRoom().getTeacher();
            Student student = schedule.getStudyRoom().getStudent();
            //최신알림장 가져오기, 없으면 -1
            Long targetId = getLatestNotebookId(schedule);

            if (isNotificationEnabled(teacher.getId(), "TEACHER", "NOTEBOOK")) {
                fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher.getFcmToken(), student.getName(), "NOTEBOOK", schedule.getSession(), targetId));
            }
        }
    }
    
    //최신 알림장 가져오기
    private Long getLatestNotebookId(Schedule schedule) {
        Schedule latestSchedule = scheduleRepository.searchByStudyRoomAndLatestDate(schedule.getStudyRoom());
        if (latestSchedule != null) {
            Notebook notebook = notebookRepository.findBySchedule(latestSchedule);
            if (notebook != null) {
                return notebook.getId();
            }
        }
        return -1L;
    }
    //하루전 숙제 안한게 있으면
    private void addHomeworkNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        LocalDate homeworkDate = currentDateTime.toLocalDate().plusDays(1);
        Timestamp hwTargetDate = Timestamp.from(homeworkDate.atTime(currentDateTime.toLocalTime()).toInstant(ZoneOffset.UTC));

        List<Notebook> notebookList = notebookRepository.serachNoteBookByDate(hwTargetDate);

        for (Notebook notebook : notebookList) {
            if (hasPendingHomework(notebook)) {
                Schedule schedule = notebook.getSchedule();
                Teacher teacher = schedule.getStudyRoom().getTeacher();
                Student student = schedule.getStudyRoom().getStudent();

                if (isNotificationEnabled(teacher.getId(), "TEACHER", "HOMEWORK")) {
                    fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher.getFcmToken(), student.getName(), "HOMEWORK", schedule.getSession(), 0L));
                }
            }
        }
    }
    //안한 숙제 있는지 확인
    private boolean hasPendingHomework(Notebook notebook) {
        return homeworkRepository.findAllByNotebook(notebook).stream().anyMatch(homework -> !homework.getIsDone());
    }
    private boolean isNotificationEnabled(Long memberId, String role, String category) {
        return noticeSettingRepository.findByMemberIdAndRoleAndCategory(memberId, role, category).getEnabled();
    }


    //시간별 푸시 알림
    //리포트 작성 - 끝날 때
   // 알림장 작성 - 수업 끝나기 10분전
    //숙제- 하루전
    private Message makeMessage(FcmSendDto fcmSendDto) {
        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getBody())
                .build();

        Message message = Message.builder()
                .setToken(fcmSendDto.getToken())
                .setNotification(notification)
                .putData("category", fcmSendDto.getCategory())
                .putData("targetId", String.valueOf(fcmSendDto.getTargetId()))
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
