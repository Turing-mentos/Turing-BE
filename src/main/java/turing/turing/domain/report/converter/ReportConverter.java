package turing.turing.domain.report.converter;

import turing.turing.domain.report.Report;
import turing.turing.domain.report.dto.response.ReportResponseDto;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.studyRoom.StudyRoom;

public class ReportConverter {

    public static ReportResponseDto.ReadDto toDto(Report report) {
        return ReportResponseDto.ReadDto.builder()
                .reportId(report.getId())
                .opening(report.getOpening())
                .money(report.getMoney())
                .closing(report.getClosing())
                .feedback(report.getFeedback())
                .studyProgress(report.getStudyProgress())
                .createdAt(report.getCreatedAt().toLocalDateTime())
                .updatedAt(report.getUpdatedAt().toLocalDateTime())
                .build();
    }

    public static Report toEntity(String opening, String studyProgress, String feedback, String money, String closing, Schedule schedule) {
        return Report.builder()
                .money(money)
                .studyProgress(studyProgress)
                .opening(opening)
                .closing(closing)
                .feedback(feedback)
                .schedule(schedule)
                .build();
    }

    public static ReportResponseDto.CreateDto toCreateDto(Report report) {
        return ReportResponseDto.CreateDto.builder()
                .reportId(report.getId()).build();
    }

    public static ReportResponseDto.StudentInfoDto toStudentInfoDto(StudyRoom s, int currentSession, int totalSession) {
        return ReportResponseDto.StudentInfoDto.builder()
                .studentId(s.getStudent().getId())
                .currentSession(currentSession)
                .subject(s.getSubject())
                .totalSession(totalSession)
                .firstName(s.getStudent().getFirstName())
                .lastName(s.getStudent().getLastName())
                .build();
    }
}
