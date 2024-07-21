package turing.turing.domain.report.converter;

import turing.turing.domain.report.Report;
import turing.turing.domain.report.dto.ReportResDto;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.studyRoom.StudyRoom;

public class ReportConverter {

    public static ReportResDto.ReadDto toDto(Report report) {
        return ReportResDto.ReadDto.builder()
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

    public static ReportResDto.CreateDto toCreateDto(Report report) {
        return ReportResDto.CreateDto.builder()
                .reportId(report.getId()).build();
    }

    public static ReportResDto.StudentInfoDto toStudentInfoDto(StudyRoom s, Schedule sc, int totalSession) {
        return ReportResDto.StudentInfoDto.builder()
                .studentId(s.getStudent().getId())
                .currentSession(sc.getSession())
                .subject(s.getSubject())
                .totalSession(totalSession)
                .name(sc.getStudentName())
                .build();
    }
}
