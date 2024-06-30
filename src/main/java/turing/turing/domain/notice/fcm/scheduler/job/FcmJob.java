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
//            switch (){
//
//            }
            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(fcmSendItem.getDvcTkn())
                    .title("푸시 메시지입니다!")
                    .body("계획된 시간이 되었어요!")
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
