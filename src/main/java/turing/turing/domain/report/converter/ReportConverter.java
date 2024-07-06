package turing.turing.domain.report.converter;

import turing.turing.domain.gpt.dto.GPTResponse;
import turing.turing.domain.report.Report;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.schedule.Schedule;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {
    public static Report toEntity(GPTResponse gptResponse, Schedule schedule){
        return Report.builder()
                .money(gptResponse.getChoices().get(0).getMessage().getContent())
                .studyProgress(gptResponse.getChoices().get(0).getMessage().getContent())
                .opening(gptResponse.getChoices().get(0).getMessage().getContent())
                .closing(gptResponse.getChoices().get(0).getMessage().getContent())
                .feedback(gptResponse.getChoices().get(0).getMessage().getContent())
                .schedule(schedule).build();
    }



    public static ReportResDto.ReadDto toDto(Report report) {
        return ReportResDto.ReadDto.builder()
                .reportId(report.getId())
                .opening(report.getOpening())
                .money(report.getMoney())
                .closing(report.getClosing())
                .feedback(report.getFeedback())
                .studyProgress(report.getStudyProgress())
                .build();
    }

    public static Report toEntity(String opening, String studyProgress, String feedback, String money, String closing, Schedule schedule) {
        return Report.builder()
                .money(money)
                .studyProgress(studyProgress)
                .opening(opening)
                .closing(closing)
                .feedback(feedback)
                .schedule(schedule).build();
    }

    public static List<ReportResDto.ReadListDto> toDtoList(List<Report> reportList) {
        return reportList.stream()
                .map(ReportConverter::toReadListDto)
                .collect(Collectors.toList());
    }

    private static ReportResDto.ReadListDto toReadListDto(Report report) {
        return ReportResDto.ReadListDto.builder()
                .reportId(report.getId())
                .name(report.getSchedule().getStudentName())
                .subject(report.getSchedule().getSubject())
                .session(report.getSchedule().getSession())
                .build();
    }

    public static ReportResDto.CreateDto toCreateDto(Report report) {
        return ReportResDto.CreateDto.builder()
                .reportId(report.getId()).build();
    }
}
