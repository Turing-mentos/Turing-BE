package turing.turing.domain.report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.gpt.dto.response.GPTResponseDto;
import turing.turing.domain.gpt.GptService;
import turing.turing.domain.gpt.PromptGenerator;
import turing.turing.domain.member.Role;
import turing.turing.domain.report.converter.ReportConverter;
import turing.turing.domain.report.dto.response.ReportReadAllDto;
import turing.turing.domain.report.dto.request.ReportRequestDto;
import turing.turing.domain.report.dto.response.ReportResponseDto;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.time.LocalDateTime;
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
    public ReportResponseDto.CreateDto createReport(CustomUserDetails userDetails, ReportRequestDto.CreateDto reportReq) {
        //과외 공간 찾기
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        Long teacherId = userDetails.getMemberId();

        StudyRoom studyRoom = studyRoomRepository.findByTeacherIdAndStudentId(teacherId, reportReq.getStudentId());

        //회차를 얻기 위해
        Schedule schedule = scheduleRepository.searchByStudyRoomIdAndLatest(studyRoom.getId());

        if(schedule == null){
            throw new RestApiException(CommonErrorCode.NO_SCHEDULE);
        }
        if (reportReq.isPay()) {
            ReportRequestDto.PayDto payDto = generatePayDto(reportReq, studyRoom);
            return processPaymentReport(reportReq, schedule, payDto);
        } else {
            return processNonPaymentReport(reportReq, schedule);
        }
    }

    // 과외비 있는 경우 처리
    private ReportResponseDto.CreateDto processPaymentReport(ReportRequestDto.CreateDto reportReq, Schedule schedule, ReportRequestDto.PayDto payDto) {
        String prompt1 = PromptGenerator.generatePrompt1(reportReq);
        String prompt2 = PromptGenerator.generatePrompt2(reportReq, payDto);
        return processReportCreation(schedule, prompt1, prompt2, null);
    }
    // 과외비 없는 경우 처리
    private ReportResponseDto.CreateDto processNonPaymentReport(ReportRequestDto.CreateDto reportReq, Schedule schedule) {
        String prompt1 = PromptGenerator.generatePrompt1(reportReq);
        String prompt3 = PromptGenerator.generatePrompt3(reportReq);
        return processReportCreation(schedule, prompt1, null, prompt3);
    }

    private ReportResponseDto.CreateDto processReportCreation(Schedule schedule, String prompt1, String prompt2, String prompt3) {
        try{
            GPTResponseDto gptResponse1 = gptService.getGptResponse(prompt1);
            GPTResponseDto gptResponse2 = prompt2 != null ? gptService.getGptResponse(prompt2) : null;
            GPTResponseDto gptResponse3 = prompt3 != null ? gptService.getGptResponse(prompt3) : null;

            String opening = gptService.parseData(gptResponse1, "[인사말]");
            String studyProgress = gptService.parseData(gptResponse1, "[지난 수업 진행 방식]");
            String feedback = null;
            String money = null;
            String closing = null;

            if(prompt2 != null){
                feedback = gptService.parseData(gptResponse2, "[학생 피드백]") ;
                money = gptService.parseData(gptResponse2, "[과외비]") ;
                closing = gptService.parseData(gptResponse2, "[마무리 멘트]") ;
            }
            else if(prompt3 != null){
                feedback = gptService.parseData(gptResponse3, "[학생 피드백]") ;
                closing = gptService.parseData(gptResponse3, "[마무리 멘트]") ;
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
    private ReportRequestDto.PayDto generatePayDto(ReportRequestDto.CreateDto reportReq, StudyRoom studyRoom) {

        //오늘날짜 이후에 baseSession만큼 schduleList 가져오기
        List<Schedule> scheduleList = scheduleRepository.findSchedulesInRange(studyRoom.getBaseSession());

        //요일별 시간과 임금 계산
        int wage = calculatePay(scheduleList, studyRoom.getWage());
        ReportRequestDto.PayDto payDto = ReportRequestDto.PayDto
                .builder()
                .wage(wage)
                .build();
        return payDto;
    }

    private int calculatePay(List<Schedule> scheduleList, int wage) {
        int pay = 0;

        for (Schedule schedule : scheduleList) {
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



    public ReportResponseDto.ReadDto readReport(CustomUserDetails userDetails, Long reportId) {
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        //사용자가 작성한 report 가져오기
        Long teacherId = userDetails.getMemberId();
        Report report = reportRepository.findReportByTeacherIdAndId(teacherId, reportId);
        if(report ==null){
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }

        ReportResponseDto.ReadDto reportResDto = ReportConverter.toDto(report);

        return reportResDto;
    }


    @Transactional
    public void updateReport(CustomUserDetails userDetails, ReportRequestDto.UpdateDto updateDto) {
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        //사용자가 작성한 report 가져오기
        Long teacherId = userDetails.getMemberId();
        Report report = reportRepository.findReportByTeacherIdAndId(teacherId, updateDto.getReportId());
        if(report ==null){
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }

        report.updateField(updateDto.getParagraphNum(), updateDto.getContent());
    }

    public List<ReportReadAllDto> readAllReport(CustomUserDetails userDetails) {
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        Long teacherId = userDetails.getMemberId();
        return reportRepository.findAllReportsByTeacherId(teacherId);
    }

    public Boolean checkConditionForReport(CustomUserDetails userDetails) {
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        Long teacherId = userDetails.getMemberId();
        return  studyRoomRepository.existsByTeacherId(teacherId);
    }

    public List<ReportResponseDto.StudentInfoDto> checkStudentInfoForReport(CustomUserDetails userDetails) {
        if (userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        Long teacherId = userDetails.getMemberId();

        List<StudyRoom> studyRoomList= studyRoomRepository.findAllByTeacherId(teacherId);

        List<ReportResponseDto.StudentInfoDto> list = new ArrayList<>();
        for (StudyRoom s : studyRoomList) {
            List<Schedule> schedules = scheduleRepository.findAllByStudyRoom(s);

            // Find the most recent schedule
            Schedule mostRecentSchedule = null;
            LocalDateTime now = LocalDateTime.now(); // Current date and time
            for (Schedule schedule : schedules) {
                if ((schedule.getDate().isBefore(now.toLocalDate()) ||
                                (schedule.getDate().isEqual(now.toLocalDate()) && schedule.getEndTime().isBefore(now.toLocalTime())))) {
                    mostRecentSchedule = schedule;
                }
            }

            int totalSession = s.getBaseSession();
            if (mostRecentSchedule != null) {
                list.add(ReportConverter.toStudentInfoDto(s, mostRecentSchedule.getSession(), totalSession));
            } else {
                if (!schedules.isEmpty()) {
                    list.add(ReportConverter.toStudentInfoDto(s, 0, totalSession));
                }
            }
        }
        return list;

    }
}
