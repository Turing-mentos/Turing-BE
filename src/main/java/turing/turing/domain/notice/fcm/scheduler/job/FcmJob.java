package turing.turing.domain.notice.fcm.scheduler.job;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import turing.turing.domain.notice.Notice;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.fcm.FcmService;
import turing.turing.domain.notice.fcm.dto.FcmSendDeviceDto;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.io.IOException;
import java.util.List;

@Slf4j
public class FcmJob implements Job {


    private  NoticeRepository noticeRepository;
    private FcmService fcmService;

    @Override
    public void execute(JobExecutionContext context) {
        if (fcmService == null) {

            //Service 인터페이스를 호출하기 위해 ApplicationContext에 appContext 이름으로 bean을 등록
            ApplicationContext appCtx = (ApplicationContext) context.getJobDetail().getJobDataMap().get("appContext");
            fcmService = appCtx.getBean(FcmService.class);
        }
        if(noticeRepository ==null){
            ApplicationContext appCtx = (ApplicationContext) context.getJobDetail().getJobDataMap().get("appContext");
            noticeRepository = appCtx.getBean(NoticeRepository.class);
        }
//FCM 전송 리스트 구성.

        List<FcmSendDeviceDto> selectFcmSendList = fcmService.selectFcmSendList();

        for (FcmSendDeviceDto fcmSendItem : selectFcmSendList) {
            //FCM 전송 데이터를 구성.
            FcmSendDto fcmSendDto = buildFcmSendDto(fcmSendItem);
            try {
                fcmService.sendMessageTo(fcmSendDto);
                saveNoticeToDatabase(fcmSendItem, fcmSendDto);
            } catch (IOException | FirebaseMessagingException e) {
                log.error("Error sending FCM message: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
    private String getBodyByCategory(FcmSendDeviceDto fcmSendItem) {
        String senderName = fcmSendItem.getSenderName();
        int session = fcmSendItem.getSession();
        switch (fcmSendItem.getCategory()) {
            case "NOTEBOOK":
                return String.format("%s학생의 %d회차 수업이 끝났어요.\n새로운 알림장을 전달해보세요.", senderName, session);
            case "HOMEWORK":
                return String.format("%s학생이 아직 숙제를 다 하지 못했어요.\n수업 전까지 숙제를 끝낼 수 있도록 독려해주세요.", senderName);
            case "REPORT":
                return String.format("%s학생의 기준 회차를 모두 끝냈어요.\n리포트를 작성하고 학부모님께 전달해주세요.", senderName);
            case "SESSION":
                return String.format("%s학생의 기준 회차를 모두 끝냈어요.\n새 수업 일정을 등록해보세요!.", senderName);
            default:
                throw new RestApiException(CommonErrorCode.NOTIFICATION_CATEGORY_NOT_FOUND);
        }
    }
    private FcmSendDto buildFcmSendDto(FcmSendDeviceDto fcmSendItem) {
        String title = getTitleByCategory(fcmSendItem.getCategory());
        String body = getBodyByCategory(fcmSendItem);

        return FcmSendDto.builder()
                .token(fcmSendItem.getDvcTkn())
                .title(title)
                .body(body)
                .category(fcmSendItem.getCategory())
                .targetId(fcmSendItem.getTargetId())
                .build();
    }
    private String getTitleByCategory(String category) {
        switch (category) {
            case "NOTEBOOK":
                return "알림장 작성하기";
            case "HOMEWORK":
                return "숙제 알리미";
            case "REPORT":
                return "리포트 작성하기";
            case "SESSION":
                return "기준 회차 추가하기";
            default:
                throw new RestApiException(CommonErrorCode.NOTIFICATION_CATEGORY_NOT_FOUND);
        }
    }
    private void saveNoticeToDatabase(FcmSendDeviceDto fcmSendItem, FcmSendDto fcmSendDto) {
        Notice notice = Notice.builder()
                .body(fcmSendDto.getBody())
                .title(fcmSendDto.getTitle())
                .senderId(fcmSendItem.getSenderId())
                .senderRole("STUDENT")
                .receiverId(fcmSendItem.getReceiverId())
                .receiverRole("TEACHER")
                .targetId(fcmSendItem.getTargetId())
                .readStatus(false)
                .category(fcmSendItem.getCategory())
                .build();
        noticeRepository.save(notice);
    }
}
