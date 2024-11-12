package turing.turing.domain.notice.fcm.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import turing.turing.domain.comment.CommentRepository;
import turing.turing.domain.member.Role;
import turing.turing.domain.notice.Notice;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.fcm.FcmService;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import turing.turing.domain.notice.fcm.dto.NotificationContent;
import turing.turing.domain.notice.fcm.dto.NotificationDetails;
import turing.turing.domain.noticeSetting.NoticeSetting;
import turing.turing.domain.noticeSetting.NoticeSettingRepository;
import turing.turing.domain.question.QuestionRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@Aspect
@AllArgsConstructor
public class AfterAspect {

    private final FcmService fcmService;
    private final NoticeSettingRepository noticeSettingRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final NoticeRepository noticeRepository;
    private final StudyRoomRepository studyRoomRepository;


    @Pointcut("execution(* createQuestion(..)) || execution(* turing.turing.domain.comment.CommentService.createComment(..)) || execution(* remindNoteBook(..)) || execution(* modifySchedules(..)) || execution(* createAlterSchedules(..)) || execution(* createExamSchedules(..)) || execution(* createNotebooks(..))")
    public void pointcut() {}


    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void handleAfterReturning(JoinPoint joinPoint, Object result) throws NoSuchFieldException, IllegalAccessException, IOException, FirebaseMessagingException {

        NotificationDetails notificationDetails = extractNotificationDetails(result);
        String notiCategory = getCategory(joinPoint.getSignature().getName());

        //해당 알림 켜져있을 떄만 작동
        NoticeSetting noticeSetting = noticeSettingRepository.findByMemberIdAndRoleAndCategory(notificationDetails.getReceiverId(), notificationDetails.getReceiverRole(), notiCategory);

        String senderName = getSenderName(notificationDetails.getSenderRole(), notificationDetails.getSenderId(), notificationDetails.getReceiverId());
        NotificationContent content = buildNotificationContent(notiCategory, senderName, notificationDetails.getSenderRole(), result);
        if (noticeSetting.getEnabled()) {
            String fcmToken = getFcmToken(notificationDetails.getReceiverRole(), notificationDetails.getReceiverId());
            sendFcmNotification(fcmToken, content, notiCategory);
        }
        saveNotice(notificationDetails, content, notiCategory);
    }

    private String getCategory(String methodName) {
        switch (methodName) {
            case "createComment":
                return  "COMMENT";
            case "createQuestion":
                return  "QUESTION";
            case "modifySchedules", "createAlterSchedules":
                return  "SCHEDULE_CHANGE";
            case "createExamSchedules":
                return  "NEW_SCHEDULE";
            case "createNotebooks":
                return "NOTEBOOK";
            case "remindNoteBook":
                return "HOMEWORK";
        }
        return methodName;
    }


    private NotificationDetails extractNotificationDetails(Object result) throws NoSuchFieldException, IllegalAccessException {
        Field senderIdField = result.getClass().getSuperclass().getDeclaredField("senderId");
        Field senderRoleField = result.getClass().getSuperclass().getDeclaredField("senderRole");
        Field receiverIdField = result.getClass().getSuperclass().getDeclaredField("receiverId");
        Field receiverRoleField = result.getClass().getSuperclass().getDeclaredField("receiverRole");

        senderIdField.setAccessible(true);
        senderRoleField.setAccessible(true);
        receiverIdField.setAccessible(true);
        receiverRoleField.setAccessible(true);

        Long senderId = (Long) senderIdField.get(result);
        Role senderRole = (Role) senderRoleField.get(result);
        Long receiverId = (Long) receiverIdField.get(result);
        Role receiverRole = (Role) receiverRoleField.get(result);

        senderIdField.setAccessible(false);
        senderRoleField.setAccessible(false);
        receiverIdField.setAccessible(false);
        receiverRoleField.setAccessible(false);

        return new NotificationDetails(senderId, senderRole, receiverId, receiverRole);
    }

    private String getFcmToken(Role receiverRole, Long receiverId) {
        if (Role.TEACHER == receiverRole) {
            return teacherRepository.findById(receiverId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getFcmToken();
        } else if (Role.STUDENT ==receiverRole) {
            return studentRepository.findById(receiverId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getFcmToken();
        } else {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
    }

    private String getSenderName(Role senderRole, Long senderId, Long receiverId) {
        if (Role.TEACHER == senderRole) {
            Teacher teacher = teacherRepository.findById(senderId).orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
            StudyRoom studyRoom = studyRoomRepository.findByTeacherIdAndStudentId(senderId, receiverId);
            return studyRoom.getSubject()+" "+teacher.getLastName()+teacher.getFirstName()+"T";

        } else if (Role.STUDENT == senderRole) {
            Student student = studentRepository.findById(senderId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
            return student.getLastName()+student.getFirstName();
        } else {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
    }
    private NotificationContent buildNotificationContent(String notiCategory, String senderName,Role senderRole, Object result) throws NoSuchFieldException, IllegalAccessException {
        String title = null;
        String body = null;
        Long targetId = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(E)").withLocale(Locale.forLanguageTag("ko-KR"));

        switch (notiCategory) {
            case "COMMENT":
                if(Role.TEACHER == senderRole){
                    title = "질문 답변";
                    body = "작성한 질문에 "+senderName + "가 댓글을 달았어요.";
                }
                else{
                    title = "새로운 댓글";
                    body = senderName + " 학생이 새로운 댓글을 남겼어요.";
                }
                targetId = (Long) getFieldValue(result, "questionId");
                break;
            case "QUESTION":
                title = "새로운 질문";
                String category = (String) getFieldValue(result, "category");
                body = senderName + " 학생이 [" + category + "] 질문을 남겼어요";
                break;
            case "SCHEDULE_CHANGE":
                LocalDate scheduleDate = (LocalDate) getFieldValue(result, "prevDate");
                if(senderRole == Role.TEACHER){
                    LocalDate alternativeDate1 = (LocalDate) getFieldValue(result, "alterDate");
                    title = "일정 변동 확정";
                    body = senderName+" ["+scheduleDate.format(formatter)+"] 수업이 ["+alternativeDate1.format(formatter)+"]로 변경되었어요.";
                }else{
                    title = "수업 일정 변경 요청";
                    body = senderName + " 학생이 ["+scheduleDate.format(formatter)+ "] 수업을 옮기고 싶어해요.";
                }
                break;
            case "NEW_SCHEDULE":
                title = "학생의 새로운 시험 일정";
                body = senderName + " 학생이 시험 일정을 등록했어요.";
                break;
            case "NOTEBOOK":
                title = "알림장 업데이트";
                body = senderName + " 수업의 알림장이 도착했어요.";
                break;
            case "HOMEWORK":
                title = "숙제 콕 찌르기";
                body = senderName + "가 지켜보고 있어요!\n 수업 전까지 "+senderName.split(" ")[0]+" 숙제를 모두 완료해주세요.";
                break;
        }

        return new NotificationContent(title, body, targetId);
    }

    private Object getFieldValue(Object result, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = result.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object fieldValue = field.get(result);
        field.setAccessible(false);
        return fieldValue;
    }

    private void sendFcmNotification(String fcmToken, NotificationContent content, String notiCategory) throws IOException, FirebaseMessagingException {
        FcmSendDto fcmSendDto = FcmSendDto.builder()
                .token(fcmToken)
                .title(content.getTitle())
                .body(content.getBody())
                .category(notiCategory)
                .targetId(content.getTargetId())
                .build();
        fcmService.sendMessageTo(fcmSendDto);
    }

    private void saveNotice(NotificationDetails details, NotificationContent content, String notiCategory) {
        Notice notice = Notice.builder()
                .body(content.getBody())
                .title(content.getTitle())
                .senderId(details.getSenderId())
                .senderRole(details.getSenderRole())
                .receiverId(details.getReceiverId())
                .receiverRole(details.getReceiverRole())
                .targetId(content.getTargetId())
                .readStatus(false)
                .category(notiCategory)
                .build();
        noticeRepository.save(notice);
    }


}
