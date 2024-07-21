package turing.turing.domain.alternativeSchedule;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.alternativeSchedule.dto.AllAlterSchedules;
import turing.turing.domain.alternativeSchedule.dto.CreateAlterScheduleRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alter-schedule")
public class AlternativeController {

    private final AlternativeService alternativeService;

    @GetMapping("/all")
    public ResponseEntity<List<AllAlterSchedules>> getAllAlterSchedules(@RequestParam("studyRoomIds") List<Long> studyRoomIds) {

        return ResponseEntity.ok(alternativeService.getAllAlterSchedules(studyRoomIds));
    }

    @PostMapping("/")
    public ResponseEntity<CreateAlterScheduleRequest> createAlterSchedule(@RequestBody CreateAlterScheduleRequest request) {
        Long firstSavedId = alternativeService.createAlterSchedule(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{alterScheduleId}")
                .buildAndExpand(firstSavedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
