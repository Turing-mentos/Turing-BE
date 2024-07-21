package turing.turing.domain.notice.fcm;

import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.HomeworkRepository;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notebook.NotebookRepository;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.fcm.dto.FcmSendDeviceDto;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import com.google.firebase.messaging.Notification;
import turing.turing.domain.notice.fcm.dto.TestDto;
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
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FcmServiceImpl implements FcmService{

    private final FirebaseMessaging firebaseMessaging;
    private final HomeworkRepository homeworkRepository;
    private final ScheduleRepository scheduleRepository;
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
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        log.info("알림장 수업 끝나기 5분전에 알려주기-------");
        addNotebookNotifications(fcmSendDeviceDtos, currentDateTime);
        log.info("하루전 숙제 안했으면 알려주기-------");
        addHomeworkNotifications(fcmSendDeviceDtos, currentDateTime);
        log.info("마지막 회차면 레포트 알림주기");
        addReportNotifications(fcmSendDeviceDtos,currentDateTime);
        log.info("마지막 회차면 세션 추가 알림 주기");
        addSessionNotifications(fcmSendDeviceDtos,currentDateTime);
        return fcmSendDeviceDtos;

    }

    private void addReportNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        addNotificationForLastSession(fcmSendDeviceDtos, currentDateTime, "REPORT", "REPORT");
    }

    private void addSessionNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        addNotificationForLastSession(fcmSendDeviceDtos, currentDateTime.minusHours(1L), "SESSION", "SESSION");
    }
    private void addNotificationForLastSession(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime, String category, String notificationType) {
        LocalDateTime targetDateTime = currentDateTime.withSecond(0).withNano(0);
        LocalDate targetDate = targetDateTime.toLocalDate();
        LocalTime targetTime = targetDateTime.toLocalTime();

        List<Schedule> scheduleList = scheduleRepository.searchScheduleByDateAndTime(targetDate, targetTime.getHour(), targetTime.getMinute());

        for (Schedule schedule : scheduleList) {
            if (schedule.getStudyRoom().getBaseSession() == schedule.getSession()) {
                Teacher teacher = schedule.getStudyRoom().getTeacher();
                Student student = schedule.getStudyRoom().getStudent();

                boolean isNotificationEnabled = false;
                if (notificationType.equals("REPORT")) {
                    isNotificationEnabled = isNotificationEnabled(teacher.getId(), "TEACHER", "REPORT");
                } else if (notificationType.equals("SESSION")) {
                    isNotificationEnabled = isNotificationEnabled(teacher.getId(), "TEACHER", "SESSION")
                            //다음 스케줄이 없음 - 기준회차 등록 안했다는 뜻
                            && !scheduleRepository.existsLatestScheduleAfterDate(targetDate, schedule.getStudyRoom().getId());
                }

                if (isNotificationEnabled) {
                    fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher, student, category, schedule.getSession(), 0L));
                }
            }
        }
    }

    private FcmSendDeviceDto buildFcmSendDeviceDto(Teacher teacher, Student student, String category, int session, Long targetId) {
        return FcmSendDeviceDto.builder()
                .dvcTkn(teacher.getFcmToken())
                .senderName(student.getLastName()+student.getFirstName())
                .category(category)
                .session(session)
                .receiverId(teacher.getId())
                .senderId(student.getId())
                .targetId(targetId)
                .build();
    }
    //알림장 수업 끝나기 5분전에 알려주기
    private void addNotebookNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        LocalDateTime targetDateTime = currentDateTime.plusMinutes(5).withSecond(0).withNano(0);
        LocalDate targetDate = targetDateTime.toLocalDate();
        LocalTime targetTime = targetDateTime.toLocalTime();

        List<Schedule> scheduleList = scheduleRepository.searchScheduleByDateAndTime(targetDate, targetTime.getHour(), targetTime.getMinute());

        for (Schedule schedule : scheduleList) {
            Teacher teacher = schedule.getStudyRoom().getTeacher();
            Student student = schedule.getStudyRoom().getStudent();
            //최신알림장 가져오기, 없으면 -1
            Long targetId = getLatestNotebookId(schedule.getStudyRoom());

            log.info("알림 켜져 있는지 확인");
            if (isNotificationEnabled(teacher.getId(), "TEACHER", "NOTEBOOK")) {
                fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher, student, "NOTEBOOK", schedule.getSession(), targetId));
            }
        }
    }
    
    //최신 알림장 가져오기
    private Long getLatestNotebookId(StudyRoom studyRoom) {
        Notebook notebook = notebookRepository.findLatestNotebookByStudyRoomId(studyRoom.getId());
        if (notebook != null) {
            return notebook.getId();
        }
        return -1L;
    }
    //하루전 숙제 안한게 있으면
    private void addHomeworkNotifications(List<FcmSendDeviceDto> fcmSendDeviceDtos, LocalDateTime currentDateTime) {
        LocalDate homeworkDate = currentDateTime.toLocalDate().plusDays(1);
        LocalDateTime hwTargetDateTime = homeworkDate.atTime(currentDateTime.toLocalTime());
        Timestamp hwTargetDate = Timestamp.valueOf(hwTargetDateTime);



        List<Notebook> notebookList = notebookRepository.searchNotebooksByDate(hwTargetDate);

        for (Notebook notebook : notebookList) {
            if (hasPendingHomework(notebook)) {
                Schedule schedule = notebook.getSchedule();
                Teacher teacher = schedule.getStudyRoom().getTeacher();
                Student student = schedule.getStudyRoom().getStudent();

                if (isNotificationEnabled(teacher.getId(), "TEACHER", "HOMEWORK")) {
                    fcmSendDeviceDtos.add(buildFcmSendDeviceDto(teacher, student, "HOMEWORK", schedule.getSession(), 0L));
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
   // 알림장 작성 - 수업 끝나기 5분전
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

//    //테스트용
//    public TestDto methodName7() {
//        log.info("여기");
//        TestDto f = TestDto.builder()
//                .category("문풀")
//                .questionId(10L)
//                .receiverRole("STUDENT")
//                .receiverId(1L)
//                .senderId(1L)
//                .senderRole("TEACHER")
//                .scheduleDate(LocalDate.now())
//                .alternativeDate(LocalDate.now())
//                .build();
//        return f;
//    }

}
