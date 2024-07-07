package turing.turing.domain.report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.gpt.dto.GPTResponse;
import turing.turing.domain.gpt.GptService;
import turing.turing.domain.gpt.PromptGenerator;
import turing.turing.domain.report.converter.ReportConverter;
import turing.turing.domain.report.dto.ReportReadAllDto;
import turing.turing.domain.report.dto.ReportReqDto;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final GptService gptService;
    private final ReportRepository reportRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final ScheduleRepository scheduleRepository;
//    private final
    public ReportResDto.CreateDto createReport(ReportReqDto.CreateDto reportReq) {

        //회차를 얻기 위해
        Schedule schedule = scheduleRepository.searchByStudyRoomIdAndLatest(reportReq.getStudyRoomId());

        if (reportReq.isPay()) {
            ReportReqDto.PayDto payDto = generatePayDto(reportReq);
            return processPaymentReport(reportReq, schedule, payDto);
        } else {
            return processNonPaymentReport(reportReq, schedule);
        }
    }

    // 과외비 있는 경우 처리
    private ReportResDto.CreateDto processPaymentReport(ReportReqDto.CreateDto reportReq, Schedule schedule, ReportReqDto.PayDto payDto) {
        String prompt1 = PromptGenerator.generatePrompt1(reportReq);
        String prompt2 = PromptGenerator.generatePrompt2(reportReq, payDto);
        return processReportCreation(reportReq, schedule, prompt1, prompt2, null);
    }
    // 과외비 없는 경우 처리
    private ReportResDto.CreateDto processNonPaymentReport(ReportReqDto.CreateDto reportReq, Schedule schedule) {
        String prompt1 = PromptGenerator.generatePrompt1(reportReq);
        String prompt3 = PromptGenerator.generatePrompt3(reportReq);
        return processReportCreation(reportReq, schedule, prompt1, null, prompt3);
    }

    private ReportResDto.CreateDto processReportCreation(ReportReqDto.CreateDto reportReq, Schedule schedule, String prompt1, String prompt2, String prompt3) {
        try{
            GPTResponse gptResponse1 = gptService.getGptResponse(prompt1);
            GPTResponse gptResponse2 = prompt2 != null ? gptService.getGptResponse(prompt2) : null;
            GPTResponse gptResponse3 = prompt3 != null ? gptService.getGptResponse(prompt3) : null;

            String opening = gptService.parseData(gptResponse1, "[인사말]");
            String studyProgress = gptService.parseData(gptResponse1, "[지난 수업 진행 방식]");
            String feedback = null;
            String money = null;
            String closing = null;

            if(prompt2== null){
                feedback = gptService.parseData(gptResponse3, "[학생 피드백]") ;
                money = gptService.parseData(gptResponse3, "[과외비]") ;
                closing = gptService.parseData(gptResponse3, "[마무리 멘트]") ;
            }
            else if(prompt3 == null){
                feedback = gptService.parseData(gptResponse2, "[학생 피드백]") ;
                 money = gptService.parseData(gptResponse2, "[과외비]") ;
                closing = gptService.parseData(gptResponse2, "[마무리 멘트]") ;
            }

            Report report = ReportConverter.toEntity(opening, studyProgress, feedback, money, closing, schedule);
            Report savedReport = reportRepository.save(report);

            return ReportConverter.toCreateDto(savedReport);

        }catch (Exception e) {
            log.error("Error creating report: " + e.getMessage(), e);
            return null;
        }
    }


    // payDto 생성, 과외비를 위한
    private ReportReqDto.PayDto generatePayDto(ReportReqDto.CreateDto reportReq) {
        StudyRoom studyRoom = studyRoomRepository.findById(reportReq.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        //오늘날짜 이후에 baseSession만큼 schduleList 가져오기
        List<Schedule> scheduleList = scheduleRepository.findSchedulesInRange(studyRoom.getBaseSession());

        //요일별 시간과 임금 계산
        int wage = calculatePay(scheduleList, studyRoom.getWage());

        ReportReqDto.PayDto payDto = ReportReqDto.PayDto
                .builder()
                .wage(wage)
                .build();
        return payDto;
    }

    private int calculatePay(List<Schedule> scheduleList, int wage) {
        int pay = 0;

        for (Schedule schedule : scheduleList) {

            log.info(String.valueOf(schedule.getId()));
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();

            // 시간 차이를 분 단위로 계산
            long startMinutes = startTime.toSecondOfDay() / 60;
            long endMinutes = endTime.toSecondOfDay() / 60;

            // 시간 차이 계산
            long timeDifference = endMinutes - startMinutes;

            // 시급을 분 단위 시간으로 곱하여 과외비 계산
            pay += wage * timeDifference;
        }
        return pay;
    }



    public ReportResDto.ReadDto readReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));

        ReportResDto.ReadDto reportResDto = ReportConverter.toDto(report);

        return reportResDto;
    }


    @Transactional
    public void updateReport(ReportReqDto.UpdateDto updateDto) {
        Report report = reportRepository.findById(updateDto.getReportId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        report.updateField(updateDto.getParagraphNum(), updateDto.getContent());
    }

    public List<ReportReadAllDto> readAllReport(Long memberId, String memberRole) {
        //Role에 따라 다르게 보여줘야 되는지는 학생 ui 나오면 결정
        return reportRepository.findAllReportsByTeacherId(memberId);
    }

    public Boolean checkConditionForReport(Long memberId, String memberRole) {
        return  studyRoomRepository.existsByTeacherId(memberId);
    }
}
