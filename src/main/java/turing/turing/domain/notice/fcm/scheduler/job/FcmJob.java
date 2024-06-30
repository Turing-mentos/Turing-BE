package turing.turing.domain.notice.fcm.scheduler.job;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import turing.turing.domain.notice.fcm.FcmService;
import turing.turing.domain.notice.fcm.dto.FcmSendDeviceDto;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;

import java.io.IOException;
import java.util.List;

@Slf4j
public class FcmJob implements Job {


    private FcmService fcmService;

    @Override
    public void execute(JobExecutionContext context) {
        if (fcmService == null) {

            //Service 인터페이스를 호출하기 위해 ApplicationContext에 appContext 이름으로 bean을 등록
            ApplicationContext appCtx = (ApplicationContext) context.getJobDetail().getJobDataMap().get("appContext");
            fcmService = appCtx.getBean(FcmService.class);

        }
//FCM 전송 리스트 구성.
        List<FcmSendDeviceDto> selectFcmSendList = fcmService.selectFcmSendList();

        for (FcmSendDeviceDto fcmSendItem : selectFcmSendList) {
            //FCM 전송 데이터를 구성.
            String title = null;
            String body = null;
            switch (fcmSendItem.getCategory()){
                case "NOTEBOOK":
                    title ="알림장 작성하기";
                    body = fcmSendItem.getSenderName()+"학생의 "+fcmSendItem.getSession()+"회차 수업이 끝났어요.\n" +
                            "새로운 알림장을 전달해보세요.";
                    break;
                case "HOMEWORK":
                    title ="숙제 알리미";
                    body = fcmSendItem.getSenderName()+ "학생이 아직 숙제를 다 하지 못했어요.\n수업 전까지 숙제를 끝낼 수 있도록 독려해주세요.";
                    break;

            }
            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(fcmSendItem.getDvcTkn())
                    .title(title)
                    .body(body)
                    .category(fcmSendItem.getCategory())
                    .build();
            try {// FCM 전송.
                fcmService.sendMessageTo(fcmSendDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
