package turing.turing.domain.alternativeSchedule;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.alternativeSchedule.dto.AllAlterSchedules;
import turing.turing.domain.alternativeSchedule.dto.CreateAlterScheduleRequest;
import turing.turing.domain.alternativeSchedule.dto.CreateAlterScheduleResponse;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;

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
    public ResponseEntity<CreateAlterScheduleResponse> createAlterSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateAlterScheduleRequest request) {
        CreateAlterScheduleResponse response = alternativeService.createAlterSchedules(request);
        response.setSender(customUserDetails.getMemberId(), Role.STUDENT);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{alterScheduleId}")
                .buildAndExpand(response.getFirstAlterScheduleId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
