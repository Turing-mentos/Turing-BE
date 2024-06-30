package turing.turing.domain.notice.fcm.scheduler.job;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
//실제 스케줄러가 수행되는 클래스
public class SchdulerConfiguration implements WebMvcConfigurer {
    private Scheduler scheduler;

    private final ApplicationContext applicationContext;

    @Bean
    public String testBean() {
        System.out.println("ApplicationContext injected: " + (applicationContext != null));
        return "Test Bean";
    }

    private static String APPLICATION_NAME = "appContext";


    // * FCM 전송을 위한 스케줄러 구성
    @PostConstruct
    private void configScheduler() throws SchedulerException {

        JobDataMap ctx = new JobDataMap();                  // 스케줄러에게 애플리케이션 영역을 추가.
        ctx.put(APPLICATION_NAME, applicationContext);      // 애플리케이션 영역을 "appContext"으로 지정.

        // Job 생성
        JobDetail job = JobBuilder
                .newJob(FcmJob.class)                                   // Job 구현 클래스
                .withIdentity("fcmSendJob", "fcmGroup")     // Job 이름, 그룹 지정
                .withDescription("FCM 처리를 위한 조회 Job")   // Job 설명
                .setJobData(ctx)
                .build();

        //Trigger 생성
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("fcmSendTrigger", "fcmGroup")         // Trigger 이름, 그룹 지정
                .withDescription("FCM 처리를 위한 조회 Trigger")     // Trigger 설명
                .startNow()
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(60)
                                .repeatForever())
                .build();

        // 스케줄러 생성 및 Job, Trigger 등록
        scheduler = new StdSchedulerFactory().getScheduler();
        FcmJobListener fcmJobListener = new FcmJobListener();
        scheduler.getListenerManager().addJobListener(fcmJobListener);
        scheduler.scheduleJob(job, trigger);
       scheduler.start();
    }
}
