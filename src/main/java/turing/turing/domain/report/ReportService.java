package turing.turing.domain.report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import turing.turing.domain.gpt.dto.GPTRequest;
import turing.turing.domain.gpt.dto.GPTResponse;
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

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ReportRepository reportRepository;
    private final StudyRoomRepository studyRoomRepository;

    public void createReport(ReportReqDto reportReq) {

        StudyRoom studyRoom = studyRoomRepository.findById(reportReq.getStudyRoomId()).orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));

        String prompt1 = "과외 학생에 대한 리포트를 생성할거야. 학부모 전달하기 위한 용도야!!!.존댓말로 써줘. 마무리 멘트는 필요없어\n" +
                "학생 이름은 " + reportReq.getName() +
                ", 학생 피드백으로는 " + reportReq.getComment() + "이라고 말하고 싶어.\n 인사말, 지난 수업 진행 방식 이라는 소제목 2개를 생성해 구분하여 생성해줘. 소제목에는 [] 붙여줘 \n 말투가 너무 딱딱하지 않게, 사람이 쓴 것 처럼 작성해줘";

        String prompt2 = "과외 학생에 대한 리포트를 생성할거야. 학부모 전달하기 위한 용도야!!. 존댓말로 써줘, 그리고 적극성, 성실성, 집중도를 기준으로 '" + reportReq.getAttitude() + "'에 해당 돼.\n" +
                "마무리 멘트로 학부모에게 전할 당부의 말씀으로는 '" + reportReq.getRequest() + "' 라는 내용을 말하고 싶어.\n" +
                "과외비는 '30만원'이고 입금기한은 '5월 30일'이야.\n 학생 피드백, 과외비, 마무리 멘트로 3개의 소제목을 생성해 구분하여 생성해줘.\n 소제목에는 [] 붙여줘 말투가 너무 딱딱하지 않게, 사람이 쓴 것 처럼 작성해줘";

        //명령어가 길어 gpt 정확도가 떨어져 2개로 구분함
        GPTRequest request1 = new GPTRequest(
                model, prompt1, 1, 256, 1, 2, 2);
        
        GPTRequest request2 = new GPTRequest(
                model, prompt2, 1, 256, 1, 2, 2);

        try {
            GPTResponse gptResponse1 = restTemplate.postForObject(
                    apiUrl, request1, GPTResponse.class);

            GPTResponse gptResponse2 = restTemplate.postForObject(
                    apiUrl, request2, GPTResponse.class);

            String opening = parseData(gptResponse1, "[인사말]");
            String studyProgress = parseData(gptResponse1, "[지난 수업 진행 방식]");
            String feedback = parseData(gptResponse2, "[학생 피드백]");
            String money = parseData(gptResponse2, "[과외비]");
            String closing = parseData(gptResponse2, "[마무리 멘트]");

             Report report = ReportConverter.toEntity(opening,studyProgress,feedback, money,closing, studyRoom);
             reportRepository.save(report);
        } catch (Exception e) {
            log.error("Error creating report: " + e.getMessage(), e);
            throw new RestApiException(CommonErrorCode.NOT_FOUND);

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
