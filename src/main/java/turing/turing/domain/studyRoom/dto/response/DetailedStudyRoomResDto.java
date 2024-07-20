package turing.turing.domain.studyRoom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyTime.dto.StudyTimeResDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DetailedStudyRoomResDto(
        String studentFirstName,
        String studentLastName,
        String subject,
        String studentSchool,
        String studentYear,
        List<StudyTimeResDto> studyTimes,
        Integer baseSession,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate firstSchedule,
        Integer wage,
        Integer curSession,
        Integer curBaseSession,
        Integer totalSession,
        Integer totalBaseSession
) {
    public static DetailedStudyRoomResDto of(StudyRoom studyRoom, List<Schedule> schedules) {

        // 수업 시작일
        LocalDate firstSchedule = schedules.isEmpty() ? null : schedules.get(0).getDate();

        // 현재 회차 / 총 회차 계산
        Schedule schedule = null;
        int i = schedules.size() - 1;
        while (i >= 0) {
            schedule = schedules.get(i);
            LocalDateTime scheduleDateTime = LocalDateTime.of(schedule.getDate(), schedule.getEndTime());
            if(scheduleDateTime.isBefore(LocalDateTime.now()))  // 날짜 역순으로 조회하며, 현재 시간보다 과거인 스케줄을 기준으로 현재 회차를 계산
                break;
            i--;
        }
        Integer curSession = (schedule == null || i < 0 ? 0 : schedule.getSession());
        Integer curBaseSession = (schedule == null ? 0 : studyRoom.getBaseSession());
        Integer totalSession = (schedule == null || i < 0 ? 0 : i+1);
        Integer totalBaseSession = (schedule == null ? 0: schedules.size());

        return new DetailedStudyRoomResDto(
                studyRoom.getStudent().getFirstName(),
                studyRoom.getStudent().getLastName(),
                studyRoom.getSubject(),
                studyRoom.getStudent().getSchool(),
                studyRoom.getStudent().getYear(),
                studyRoom.getStudyTimes().stream().map(StudyTimeResDto::of).toList(),
                studyRoom.getBaseSession(),
                firstSchedule,
                studyRoom.getWage(),
                curSession,
                curBaseSession,
                totalSession,
                totalBaseSession
        );
    }
}
