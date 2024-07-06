package turing.turing.domain.report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import turing.turing.domain.gpt.dto.GPTResponse;
import turing.turing.domain.gpt.GptService;
import turing.turing.domain.gpt.PromptGenerator;
import turing.turing.domain.report.converter.ReportConverter;
import turing.turing.domain.report.dto.ReportReqDto;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final GptService gptService;
    private final ReportRepository reportRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
//    private final
    public ReportResDto.CreateDto createReport(ReportReqDto.CreateDto reportReq) {

        //StudyRoom studyRoom = studyRoomRepository.findById(reportReq.getStudyRoomId())
          //      .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
        //해당 스터디룸에서 가장 날짜가 가까운 회차의 리포트로 넣어줌

        Schedule schedule = scheduleRepository.searchByStudyRoomIdAndLatest(reportReq.getStudyRoomId());

        //과외비와 날짜는 추후 반영, 아직 ui가 제대로 안나옴
        String prompt1 = PromptGenerator.generatePrompt1(reportReq);
        String prompt2 = PromptGenerator.generatePrompt2(reportReq);

        try {
            GPTResponse gptResponse1 = gptService.getGptResponse(prompt1);
            GPTResponse gptResponse2 = gptService.getGptResponse(prompt2);

            String opening = gptService.parseData(gptResponse1, "[인사말]");
            String studyProgress = gptService.parseData(gptResponse1, "[지난 수업 진행 방식]");
            String feedback = gptService.parseData(gptResponse2, "[학생 피드백]");
            String money = null;
            if(reportReq.isPay()) {

                money = gptService.parseData(gptResponse2, "[과외비]");
            }
            String closing = gptService.parseData(gptResponse2, "[마무리 멘트]");

            log.info("인사말"+ opening);
            log.info("지난 수업 진행 방식"+ studyProgress);
            log.info("학생 피드백"+ feedback);
            log.info("과외비"+ money);
            log.info("마무리 멘트"+ closing);

            Report report = ReportConverter.toEntity(opening, studyProgress, feedback, money, closing, schedule);
            Report savedReport= reportRepository.save(report);
             return ReportConverter.toCreateDto(savedReport);
        } catch (Exception e) {
            log.error("Error creating report: " + e.getMessage(), e);
        }
        return null;
    }

    public ReportResDto.ReadDto readReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));

        ReportResDto.ReadDto reportResDto = ReportConverter.toDto(report);

        return reportResDto;
    }


    public void updateReport(ReportReqDto.UpdateDto updateDto) {
        Report report = reportRepository.findById(updateDto.getReportId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
        Report updatedReport = report.updateField(updateDto.getParagraphNum(), updateDto.getContent());
        reportRepository.save(updatedReport);

    }

    public List<ReportResDto.ReadListDto> readAllReport(Long memberId, String memberRole) {
        //Role에 따라 다르게 보여줘야 되는지는 학생 ui 나오면 결정

        Teacher teacher = teacherRepository.findById(memberId).orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
        List<StudyRoom> studyRoom = studyRoomRepository.findAllByTeacher(teacher);

        List<Report> reportList = new ArrayList<>();

        for(StudyRoom s : studyRoom){
            List<Report> reports = reportRepository.findAllByStudyRoom(studyRoomRepository.findById(s.getId())
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND)));
            reportList.addAll(reports);
        }

        return  ReportConverter.toDtoList(reportList);
    }
}
