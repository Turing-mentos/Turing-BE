package turing.turing.domain.report.converter;

import turing.turing.domain.gpt.dto.GPTResponse;
import turing.turing.domain.report.Report;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {
    public static Report toEntity(GPTResponse gptResponse, StudyRoom studyRoom){
        return Report.builder()
                .money(gptResponse.getChoices().get(0).getMessage().getContent())
                .studyProgress(gptResponse.getChoices().get(0).getMessage().getContent())
                .opening(gptResponse.getChoices().get(0).getMessage().getContent())
                .closing(gptResponse.getChoices().get(0).getMessage().getContent())
                .feedback(gptResponse.getChoices().get(0).getMessage().getContent())
                .studyRoom(studyRoom).build();
    }



    public static ReportResDto toDto(Report report) {
        return ReportResDto.builder()
                .reportId(report.getId())
                .opening(report.getOpening())
                .money(report.getMoney())
                .closing(report.getClosing())
                .feedback(report.getFeedback())
                .studyProgress(report.getStudyProgress())
                .build();
    }

    public static Report toEntity(String opening, String studyProgress, String feedback, String money, String closing, StudyRoom studyRoom) {
        return Report.builder()
                .money(money)
                .studyProgress(studyProgress)
                .opening(opening)
                .closing(closing)
                .feedback(feedback)
                .studyRoom(studyRoom).build();
    }

    public static List<ReportResDto> toDtoList(List<Report> reportList) {
        return reportList.stream()
                .map(ReportConverter::toDto)
                .collect(Collectors.toList());
    }
}
