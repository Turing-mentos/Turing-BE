package turing.turing.domain.notice.fcm.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import turing.turing.domain.notice.Notice;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.fcm.FcmService;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import turing.turing.domain.notice.fcm.dto.NotificationContent;
import turing.turing.domain.notice.fcm.dto.NotificationDetails;
import turing.turing.domain.noticeSetting.NoticeSettingRepository;
import turing.turing.domain.question.QuestionRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.io.IOException;
import java.lang.reflect.Field;

@Component
@Aspect
@Slf4j
@AllArgsConstructor
public class AfterAspect {

    private final FcmService fcmService;
    private final NoticeSettingRepository noticeSettingRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final NoticeRepository noticeRepository;

    @Pointcut("execution(* test1())")
    public void pointcut() {}

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void handleAfterReturning(JoinPoint joinPoint, Object result) throws NoSuchFieldException, IllegalAccessException, IOException, FirebaseMessagingException {
        log.info("메소드", joinPoint.getSignature().getName(), result);

        NotificationDetails notificationDetails = extractNotificationDetails(result);
        String fcmToken = getFcmToken(notificationDetails.getReceiverRole(), notificationDetails.getReceiverId());
        String senderName = getSenderName(notificationDetails.getSenderRole(), notificationDetails.getSenderId());

        NotificationContent content = buildNotificationContent(joinPoint.getSignature().getName(), senderName, result);

        if (isNotificationEnabled(notificationDetails.getReceiverId(), notificationDetails.getReceiverRole(), content.getCategory())) {
            sendFcmNotification(fcmToken, content);
            saveNotice(notificationDetails, content);
        }
    }
    private NotificationDetails extractNotificationDetails(Object result) throws NoSuchFieldException, IllegalAccessException {
        Field senderIdField = result.getClass().getDeclaredField("senderId");
        Field senderRoleField = result.getClass().getDeclaredField("senderRole");
        Field receiverIdField = result.getClass().getDeclaredField("receiverId");
        Field receiverRoleField = result.getClass().getDeclaredField("receiverRole");

        senderIdField.setAccessible(true);
        senderRoleField.setAccessible(true);
        receiverIdField.setAccessible(true);
        receiverRoleField.setAccessible(true);

        Long senderId = (Long) senderIdField.get(result);
        String senderRole = (String) senderRoleField.get(result);
        Long receiverId = (Long) receiverIdField.get(result);
        String receiverRole = (String) receiverRoleField.get(result);

        senderIdField.setAccessible(false);
        senderRoleField.setAccessible(false);
        receiverIdField.setAccessible(false);
        receiverRoleField.setAccessible(false);

        return new NotificationDetails(senderId, senderRole, receiverId, receiverRole);
    }

    private String getFcmToken(String receiverRole, Long receiverId) {
        if ("TEACHER".equals(receiverRole)) {
            return teacherRepository.findById(receiverId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getFcmToken();
        } else if ("STUDENT".equals(receiverRole)) {
            return studentRepository.findById(receiverId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getFcmToken();
        } else {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
    }

    private String getSenderName(String senderRole, Long senderId) {
        if ("TEACHER".equals(senderRole)) {
            return teacherRepository.findById(senderId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getName();
        } else if ("STUDENT".equals(senderRole)) {
            return studentRepository.findById(senderId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                    .getName();
        } else {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
    }
    private NotificationContent buildNotificationContent(String methodName, String senderName, Object result) throws NoSuchFieldException, IllegalAccessException {
        String targetAlarm = null;
        String title = null;
        String body = null;
        Long targetId = 0L;

        switch (methodName) {
            case "createComment":
                targetAlarm = "COMMENT";
                title = "새로운 댓글";
                body = senderName + " 학생이 새로운 댓글을 남겼어요.";
                targetId = getFieldValue(result, "commendId");
                break;
            case "createQuestion":
                targetAlarm = "QUESTION";
                title = "새로운 질문";
                Long questionId = getFieldValue(result, "questionId");
                String category = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND))
                        .getContent();
                body = senderName + " 학생이 [" + category + "] 질문을 남겼어요";
                break;
            case "methodName3":
                targetAlarm = "SCHEDULE_CHANGE";
                title = "수업 일정 변경 요청";
                body = senderName + " 학생이 수업을 옮기고 싶어해요.";
                break;
            case "methodName4":
                targetAlarm = "NEW_SCHEDULE";
                title = "학생의 새로운 시험 일정";
                body = senderName + " 학생이 시험 일정을 등록했어요.";
                break;
        }

        return new NotificationContent(targetAlarm, title, body, targetId);
    }

    private Long getFieldValue(Object result, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = result.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Long fieldValue = (Long) field.get(result);
        field.setAccessible(false);
        return fieldValue;
    }
    private boolean isNotificationEnabled(Long receiverId, String receiverRole, String category) {
        return noticeSettingRepository.findByMemberIdAndRoleAndCategory(receiverId, receiverRole, category).getEnabled();
    }

    private void sendFcmNotification(String fcmToken, NotificationContent content) throws IOException, FirebaseMessagingException {
        FcmSendDto fcmSendDto = FcmSendDto.builder()
                .token(fcmToken)
                .title(content.getTitle())
                .body(content.getBody())
                .category(content.getCategory())
                .targetId(content.getTargetId())
                .build();
        fcmService.sendMessageTo(fcmSendDto);
    }

    private void saveNotice(NotificationDetails details, NotificationContent content) {
        Notice notice = Notice.builder()
                .body(content.getBody())
                .title(content.getTitle())
                .senderId(details.getSenderId())
                .senderRole(details.getSenderRole())
                .receiverId(details.getReceiverId())
                .receiverRole(details.getReceiverRole())
                .targetId(content.getTargetId())
                .readStatus(false)
                .build();
        noticeRepository.save(notice);
    }

    
}
