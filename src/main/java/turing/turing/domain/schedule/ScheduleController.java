package turing.turing.domain.schedule;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.schedule.dto.CreateScheduleRequest;
import turing.turing.domain.schedule.dto.ModifyScheduleRequest;
import turing.turing.domain.schedule.dto.ScheduleDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/month")
    public ResponseEntity<List<ScheduleDto>> getMonthSchedules(@RequestParam("date") LocalDate date, @RequestParam("studyRoomIds") List<Long> studyRoomIds) {

        return ResponseEntity.ok(scheduleService.getMonthSchedules(date, studyRoomIds));
    }

    @GetMapping("{scheduleId}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable("scheduleId") Long scheduleId) {

        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    @PostMapping("/")
    public ResponseEntity<Long> createSchedules(@RequestBody CreateScheduleRequest request) {

        Long firstSavedId = scheduleService.createSchedules(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{homeworkId}")
                .buildAndExpand(firstSavedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/")
    public ResponseEntity<Long> modifySchedule(@RequestBody ModifyScheduleRequest request) {

        return ResponseEntity.ok(scheduleService.modifySchedule(request));
    }

}
