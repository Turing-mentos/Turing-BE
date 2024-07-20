package turing.turing.domain.alternativeSchedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import turing.turing.domain.schedule.Schedule;

@Getter
public class ToAlterScheduleDto {

    private Long scheduleId;
    private LocalDate date;
//    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer session;
    private Integer baseSession;
    private List<AlterScheduleDto> alterScheduleList;

    public ToAlterScheduleDto(Schedule schedule, List<AlterScheduleDto> alterScheduleList) {
        this.scheduleId = schedule.getId();
        this.date = schedule.getDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.session = schedule.getSession();
        this.baseSession = schedule.getStudyRoom().getBaseSession();
        this.alterScheduleList = alterScheduleList;
    }
}
