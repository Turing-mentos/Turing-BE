package turing.turing.domain.alternativeSchedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.alternativeSchedule.converter.AlterScheduleConverter;
import turing.turing.domain.alternativeSchedule.dto.AlterScheduleDto;
import turing.turing.domain.alternativeSchedule.dto.CreateAlterScheduleRequest;
import turing.turing.domain.alternativeSchedule.dto.AllAlterSchedules;
import turing.turing.domain.alternativeSchedule.dto.CreateAlterScheduleResponse;
import turing.turing.domain.alternativeSchedule.dto.TimePairDto;
import turing.turing.domain.member.Role;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.domain.alternativeSchedule.dto.ToAlterScheduleDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlternativeService {

    private final ScheduleRepository scheduleRepository;
    private final AlternativeScheduleRepository alternativeScheduleRepository;

    //TODO 테스트를 통한 성능 측정 필요
    //현재 패치조인을 사용해 쿼리 하나로 대체스케줄-스케줄-과외공간 조회 중. (중복되는 데이터를 많이 가져오는 것 vs 쿼리 수 증가) 중 선택 필요
    public List<AllAlterSchedules> getAllAlterSchedules(List<Long> studyRoomIds) {
        // 조회일자 이후의 일정에 대한 대체스케줄 조회
        List<AlternativeSchedule> alterScheduleList = alternativeScheduleRepository.findAllByStudyRoomIds(studyRoomIds);

        List<AllAlterSchedules> result = new ArrayList<>();

        List<AlterScheduleDto> alterScheduleDtoList = new ArrayList<>();
        List<ToAlterScheduleDto> toAlterScheduleDtoList = new ArrayList<>();
        Schedule prevSchedule = alterScheduleList.get(0).getSchedule();
        Long studyRoomId = prevSchedule.getStudyRoom().getId();

        int last = alterScheduleList.size();
        for (int i = 0; i < last; i++) {
            AlternativeSchedule alternativeSchedule = alterScheduleList.get(i);
            AlterScheduleDto alterScheduleDto = AlterScheduleConverter.toDto(alternativeSchedule);
            // 다른 스케줄인 경우 - 대체스케줄 리스트를 포함하여 이전의 스케줄에 대한 객체 생성, 스케줄 리스트에 삽입, 스케줄 정보 변경, 대체스케줄 리스트 초기화
            if (!prevSchedule.equals(alternativeSchedule.getSchedule())) {
                ToAlterScheduleDto toAlterScheduleDto = new ToAlterScheduleDto(prevSchedule, alterScheduleDtoList);
                toAlterScheduleDtoList.add(toAlterScheduleDto);
                prevSchedule = alternativeSchedule.getSchedule();
                alterScheduleDtoList = new ArrayList<>();
                // 다른 과외공간인 경우 - 스케줄 리스트를 포함하여 이전의 과외공간에 대한 객체 생성, 응답결과에 추가, 스케줄리스트 초기화, 과외공간 정보 변경
                if (!studyRoomId.equals(prevSchedule.getStudyRoom().getId())) {
                    AllAlterSchedules allAlterSchedules = new AllAlterSchedules(studyRoomId, toAlterScheduleDtoList);
                    result.add(allAlterSchedules);
                    toAlterScheduleDtoList = new ArrayList<>();
                    studyRoomId = prevSchedule.getStudyRoom().getId();
                }
            }
            // ---위 로직은 이전까지의 데이터에 대한 정리---
            // 현재 대체스케줄 대체스케줄 리스트에 추가
            alterScheduleDtoList.add(alterScheduleDto);
            // 마지막인 경우
            if (i == last - 1) {
                ToAlterScheduleDto toAlterScheduleDto = new ToAlterScheduleDto(prevSchedule, alterScheduleDtoList);
                toAlterScheduleDtoList.add(toAlterScheduleDto);
                AllAlterSchedules allAlterSchedules = new AllAlterSchedules(studyRoomId, toAlterScheduleDtoList);
                result.add(allAlterSchedules);
            }
        }

        return result;
    }

    //TODO saveAll -> bulkInsert 개선 필요
    @Transactional
    public CreateAlterScheduleResponse createAlterSchedules(CreateAlterScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getTargetScheduleId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
        Long teacherId = schedule.getStudyRoom()
                .getTeacher()
                .getId();

        Map<LocalDate, List<TimePairDto>> alterScheduleList = request.getAlterScheduleList();
        Set<LocalDate> ketSet = alterScheduleList.keySet();

        List<AlternativeSchedule> result = new ArrayList<>();
        //대체 가능 날짜의
        for (LocalDate scheduleDate : ketSet) {
            List<TimePairDto> scheduleTimes = alterScheduleList.get(scheduleDate);
            //대체 가능 시간에 대해 대체스케줄 생성
            for (TimePairDto timePairDto : scheduleTimes) {
                result.add(
                        AlternativeSchedule.builder()
                        .scheduleDate(scheduleDate)
                        .startTime(timePairDto.getStartTime())
                        .endTime(timePairDto.getEndTime())
                        .schedule(schedule)
                        .build()
                );
            }
        }
        Long firstSavedId = alternativeScheduleRepository.saveAll(result).get(0).getId();

        return CreateAlterScheduleResponse.builder()
                .firstAlterScheduleId(firstSavedId)
                .scheduleDate(schedule.getDate())
                .receiverId(teacherId)
                .receiverRole(Role.TEACHER)
                .build();
    }
}
