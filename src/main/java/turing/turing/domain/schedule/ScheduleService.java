package turing.turing.domain.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.member.Role;
import turing.turing.domain.schedule.dto.CreateScheduleRequest;
import turing.turing.domain.schedule.dto.ModifyScheduleRequest;
import turing.turing.domain.schedule.dto.ModifyScheduleResponse;
import turing.turing.domain.schedule.dto.ScheduleDto;
import turing.turing.domain.schedule.dto.UpdateScheduleDto;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudentRepository studentRepository;

    public List<ScheduleDto> getMonthSchedules(LocalDate date, List<Long> studyRoomIds) {
        int month = date.getMonthValue();
        int year = date.getYear();

        return scheduleRepository.findAllByMonthAndStudyRoomIds(month, year, studyRoomIds);
    }

    public List<ScheduleDto> getWeeklySchedules(LocalDate date, List<Long> studyRoomIds) {
        LocalDate endDate = date.plusDays(10);

        return scheduleRepository.findAllByDateAndStudyRoomIds(date, endDate, studyRoomIds);
    }

    public ScheduleDto getSchedule(Long scheduleId) {

        return scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
    }

    //TODO saveAll -> bulkInsert 개선 필요
    @Transactional
    public Long createSchedules(CreateScheduleRequest request) {
        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        List<Schedule> result = new ArrayList<>();

        LocalDate startDate = request.getStartDate();
        int day = startDate.getDayOfWeek().getValue();
        String studentName = request.getStudentName();
        String subject = request.getSubject();
        int baseSession = request.getBaseSession();

        List<StudyTimeReqDto> studyTimeList = request.getStudyTimeList();

        int count = 1;
        LocalDate currentDate = startDate;
        //날짜, 시작시간, 끝시간, 학생이름, 과목, 회차, 과외공간
        while (count <= baseSession) {
            for (StudyTimeReqDto reqDto : studyTimeList) {
                // 희망요일: 월,수,금 \ 시작요일: 수 인 경우, 다음주 월요일부터 생성되는 것 방지
                if (count == 1 && day != reqDto.day()) {
                    continue;
                }
                if (count > baseSession) {
                    break;
                }

                currentDate = findNextDay(currentDate, reqDto.day());

                Schedule schedule = Schedule.builder().date(currentDate)
                        .startTime(reqDto.startTime())
                        .endTime(reqDto.endTime())
                        .studentName(studentName)
                        .subject(subject)
                        .session(count++)
                        .studyRoom(studyRoom)
                        .build();

                result.add(schedule);
                count++;

                //주 1회인 경우 날짜 갱신 용도
                currentDate = currentDate.plusDays(1);
            }
        }

        return scheduleRepository.saveAll(result).get(0).getId();
    }

    private LocalDate findNextDay(LocalDate startDate, Integer day) {
        int add = (day - startDate.getDayOfWeek().getValue() + 7) % 7;

        return startDate.plusDays(add);
    }

    @Transactional
    public ModifyScheduleResponse modifySchedules(ModifyScheduleRequest request) {
        Long scheduleId = request.getScheduleId();
        LocalDate modifiedDate = request.getDate();
        List<Schedule> scheduleList = scheduleRepository.findAllByIdAndDate(scheduleId, modifiedDate);
        if (scheduleList.isEmpty()) {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }

        List<Long> scheduleIds = scheduleList.stream()
                .map(Schedule::getId)
                .toList();

        LocalDate prevDate;
        Schedule firstSchedule = scheduleList.get(0);
        //일정 변경이 다른 일정과 엇갈리지 않는 경우
        if (scheduleList.size() == 1) {
            prevDate = firstSchedule.getDate();
            firstSchedule.update(request);
        } //일정을 나중으로 미루는 경우
        else if (firstSchedule.getId().equals(request.getScheduleId())) {
            prevDate = firstSchedule.getDate();
            scheduleRepository.moveUpSchedules(scheduleIds);

            UpdateScheduleDto dto = UpdateScheduleDto.builder()
                    .date(request.getDate())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .session(scheduleList.get(scheduleList.size() - 1).getSession())
                    .build();

            firstSchedule.updateWithSession(dto);
        } //일정을 앞으로 땡기는 경우
        else {
            scheduleRepository.postponeSchedules(scheduleIds);
            Schedule targetSchedule = scheduleList.get(scheduleList.size() - 1);
            prevDate = targetSchedule.getDate();

            UpdateScheduleDto dto = UpdateScheduleDto.builder()
                    .date(request.getDate())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .session(firstSchedule.getSession())
                    .build();

            targetSchedule.updateWithSession(dto);
        }

        Long studentId = studentRepository.findByScheduleId(scheduleId);

        return ModifyScheduleResponse.builder()
                .scheduleId(scheduleId)
                .prevDate(prevDate)
                .alterDate(modifiedDate)
                .receiverId(studentId)
                .receiverRole(Role.STUDENT)
                .build();
    }

}
