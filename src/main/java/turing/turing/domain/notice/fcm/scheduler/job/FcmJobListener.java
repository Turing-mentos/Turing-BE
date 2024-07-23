package turing.turing.domain.notice.fcm.scheduler.job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

//스케줄러를 수행하는 과정에서 라이프 사이클 확인하고 싶어 구성
public class FcmJobListener implements JobListener {
    @Override
    public String getName() {
        return "FcmJobListener"; // Providing a unique name for the JobListener
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("[-] Job이 실행되기전 수행됩니다");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("[-] Job이 실행 취소된 시점 수행됩니다.");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("[+] Job이 실행 완료된 시점 수행됩니다.");
    }
}
