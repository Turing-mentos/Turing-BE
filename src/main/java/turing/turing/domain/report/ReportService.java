package turing.turing.domain.report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import turing.turing.domain.gpt.dto.GPTRequest;
import turing.turing.domain.gpt.dto.GPTResponse;
import turing.turing.domain.gpt.service.GptService;
import turing.turing.domain.gpt.service.PromptService;
import turing.turing.domain.report.converter.ReportConverter;
import turing.turing.domain.report.dto.ReportReqDto;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final GptService gptService;
    private final PromptService promptService;
    private final ReportRepository reportRepository;
    private final StudyRoomRepository studyRoomRepository;

    public void createReport(ReportReqDto reportReq) {

        StudyRoom studyRoom = studyRoomRepository.findById(reportReq.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        //과외비와 날짜는 추후 반영, 아직 ui가 제대로 안나옴
        String prompt1 = promptService.generatePrompt1(reportReq);
        String prompt2 = promptService.generatePrompt2(reportReq);

        try {
            GPTResponse gptResponse1 = gptService.getGptResponse(prompt1);
            GPTResponse gptResponse2 = gptService.getGptResponse(prompt2);

            String opening = gptService.parseData(gptResponse1, "[인사말]");
            String studyProgress = gptService.parseData(gptResponse1, "[지난 수업 진행 방식]");
            String feedback = gptService.parseData(gptResponse2, "[학생 피드백]");
            String money = gptService.parseData(gptResponse2, "[과외비]");
            String closing = gptService.parseData(gptResponse2, "[마무리 멘트]");

            log.info("인사말"+ opening);
            log.info("지난 수업 진행 방식"+ studyProgress);
            log.info("학생 피드백"+ feedback);
            log.info("과외비"+ money);
            log.info("마무리 멘트"+ closing);

            Report report = ReportConverter.toEntity(opening, studyProgress, feedback, money, closing, studyRoom);
            reportRepository.save(report);
        } catch (Exception e) {
            log.error("Error creating report: " + e.getMessage(), e);
        }
    }

    public ReportResDto readReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));

        ReportResDto reportResDto = ReportConverter.toDto(report);

        return reportResDto;
    }

    public void deleteReport(Long reportId, int paragraphNum) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Report updatedReport = report.updateField(paragraphNum, null);
        reportRepository.save(updatedReport);
    }


    public void updateReport(Long reportId, int paragraphNum, String reportUpdateContent) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Report updatedReport = report.updateField(paragraphNum, reportUpdateContent);
        reportRepository.save(updatedReport);
    }

    private String parseData(GPTResponse gptResponse, String sectionTitle) {
        String content = "";

        for (GPTResponse.Choice choice : gptResponse.getChoices()) {
            String messageContent = choice.getMessage().getContent();

            if (messageContent.contains(sectionTitle)) {
                int startIndex = messageContent.indexOf(sectionTitle) + sectionTitle.length();
                //  ']' 확인
                if (messageContent.charAt(startIndex) == ']') {
                    startIndex++;
                }
                content = messageContent.substring(startIndex).trim();

                // section title있으면 , truncate the content
                if (content.contains("[")) {
                    content = content.substring(0, content.indexOf("[")).trim();
                }

                break;
            }
        }

        return content;
    }

    public List<ReportResDto> readAllReport(Long studyRoomId) {
        List<Report> reportList = reportRepository.findAllByStudyRoom(studyRoomRepository.findById(studyRoomId).
                orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND)));

        return  ReportConverter.toDtoList(reportList);
    }
}
