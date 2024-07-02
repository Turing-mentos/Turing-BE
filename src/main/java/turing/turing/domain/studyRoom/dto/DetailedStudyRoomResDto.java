package turing.turing.domain.studyRoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyTime.dto.StudyTimeResDto;

import java.time.LocalDate;
import java.util.List;

public record DetailedStudyRoomResDto(
        String studentName,
        String subject,
        String studentSchool,
        String studentYear,
        List<StudyTimeResDto> studyTimes,
        Integer baseSession,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate firstSchedule,
        Integer curSession,
        Integer curBaseSession,
        Integer totalSession,
        Integer totalBaseSession
) {
    public static DetailedStudyRoomResDto of(StudyRoom studyRoom, List<Schedule> schedules) {
        LocalDate firstSchedule = schedules.isEmpty() ? null : schedules.get(0).getDate();
        Integer curSession = schedules.isEmpty() ? 0 : schedules.get(schedules.size() - 1).getSession();

        return new DetailedStudyRoomResDto(
                studyRoom.getStudent().getName(),
                studyRoom.getSubject(),
                studyRoom.getStudent().getSchool(),
                studyRoom.getStudent().getYear(),
                studyRoom.getStudyTimes().stream().map(StudyTimeResDto::of).toList(),
                studyRoom.getBaseSession(),
                firstSchedule,
                curSession,
                studyRoom.getBaseSession(),
                schedules.size(),
                (schedules.size() / studyRoom.getBaseSession() + 1) * studyRoom.getBaseSession()
        );
    }
}
