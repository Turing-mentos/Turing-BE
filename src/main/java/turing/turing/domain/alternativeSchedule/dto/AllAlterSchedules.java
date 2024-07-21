package turing.turing.domain.alternativeSchedule.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllAlterSchedules {
    private Long studyRoomId;
    private List<ToAlterScheduleDto> scheduleList;
}

/**
 * 과외공간
     * 스케줄 리스트
         * 대체스케줄 리스트
 * 과외공간
     * 스케줄 리스트
         * 대체스케줄 리스트
 * studyRoomId - GetAllAlterScheduleResponse
     * scheduleId - ToAlterScheduleDto
     * date
     * day
     * startTime
     * endTime
     * session
     * baseSession
        * alterScheduleId - AlterScheduleDto
        * date
        * day
        * startTime
        * endTime
 */