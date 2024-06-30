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
import turing.turing.domain.noticeSetting.NoticeSetting;
import turing.turing.domain.noticeSetting.NoticeSettingRepository;
import turing.turing.domain.question.Question;
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
    private final  NoticeRepository noticeRepository;

    @Pointcut("execution(* test1())") //포인트 컷 설정
    public void pointcut(){}

    //수업 일정 변경 요청 - targetId(), 선생 학생인지
    //시험 일정 등록 - targetId(), 선생 학생인지
    //질문 등록 - targetId(질문 받는 상대방 id), 선생 학생인지
    //댓글 등록 - targetId(알람을 보내야하는 상대방 id)

    //senderId, senderRole) 수신자 수신자 역할(recevierId, receiverRole)
    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void test1113(JoinPoint joinPoint, Object result) throws NoSuchFieldException, IllegalAccessException, IOException, FirebaseMessagingException {

        log.info("test 성공, 반환 값: " + result.toString());
        log.info("Join"+ joinPoint.getSignature().getName());

        Field senderField = result.getClass().getDeclaredField("senderId");
        Field senderRoleField = result.getClass().getDeclaredField("senderRole");
        Field receiverIdField  = result.getClass().getDeclaredField("senderId");
        Field receiverRoleField = result.getClass().getDeclaredField("senderRole");


        //보안상 문제
        senderField.setAccessible(true);
        senderRoleField.setAccessible(true);
        receiverIdField.setAccessible(true);
        receiverRoleField.setAccessible(true);

        Long senderId = (Long) senderField.get(result);
        String senderRole = (String) senderRoleField.get(result);
        Long receiverId = (Long) receiverIdField.get(result);
        String receiverRole = (String) receiverRoleField.get(result);

        senderField.setAccessible(false);
        senderRoleField.setAccessible(false);
        receiverIdField.setAccessible(false);
        receiverRoleField.setAccessible(false);

        Student senderStudent = null;
        Teacher senderTeacher = null;
        String senderName = null;
        // 타켓 fcm token 가져오기
        String fcmToken = null;
        switch (receiverRole){
            case "TEACHER":
                Teacher teacher = teacherRepository.findById(receiverId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
                //보낸사람
                senderStudent = studentRepository.findById(senderId).orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
                senderName = senderStudent.getName();
                fcmToken = teacher.getPhone(); // 추후 fcm 으로 바꾸기
                // break;
            case "STUDENT":
                Student student = studentRepository.findById(receiverId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
                senderTeacher = teacherRepository.findById(senderId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
                senderName = senderTeacher.getName();
                fcmToken = student.getPhone(); // 추후 fcm 으로 바꾸기
            }

            String targetAlarm = null;
            String title = null;
            String body = null;

            //어디서 왔는지 확인

            // 메소드에 따라 메세지 셍성....
            switch(joinPoint.getSignature().getName()){
                // 새 질문 등록
                case "메소드이름":
                    targetAlarm = "COMMENT";
                    title = "새로운 댓글";
                    body = senderName+ "학생이 새로운 댓글을 남겼어요.";
                    //..추후 추가
                    break;
                case "메소드 이름":
                    targetAlarm = "QUESTION";
                    title= "새로운 질문";
                    Field questionField = result.getClass().getDeclaredField("questionId");
                    questionField.setAccessible(true);
                    Long questionId = (Long) questionField.get(result);
                    questionField.setAccessible(false);
                    //이거 엔티티 추가함
                    String category =questionRepository.findById(questionId).orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND)).getContent();
                    body = senderName+" 학생이 ["+category+"] 질문을 남겼어요";
                    break;
                case "메소드 이름":
                    targetAlarm = "SCHEDULE_CHANGE";
                    title ="수업 일정 변정 요청";
                    //시간 받아서 시간 받기
                    body = senderName+"학생이 ["+"("+")]수업을 옮기고 싶어해요.";
                    break;
                case "메소드 이름":
                    targetAlarm = "NEW_SCHEDULE";
                    title ="학생의 새로운 시험 일정";
                    body = senderName+"학생이 시험 일정을 등록했어요.";
                    break;

        }

        //타겟 id가 알람 설정이 켜져있는지 확인한 후
        boolean turnOn = noticeSettingRepository.findByMemberIdAndRoleAndCategory(receiverId, receiverRole, targetAlarm).getEnabled();

        if(turnOn){
            // FcmSendDto만들기
            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(fcmToken)
                    .title(title)
                    .body(body).build();

            fcmService.sendMessageTo(fcmSendDto);

            //디비에 기록 저장
            Notice notice = new Notice(senderId, senderRole, receiverId,receiverRole, 0, title, body);
            noticeRepository.save(notice);
        }
    }
}
