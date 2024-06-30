package turing.turing.domain.studyRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.studyRoom.dto.StudyRoomReqDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    @PostMapping
    public ResponseEntity<Long> createStudyRoom(@RequestBody StudyRoomReqDto studyRoomReqDto){
        Long studyRoomId = studyRoomService.createStudyRoom(1L, studyRoomReqDto);
        return ResponseEntity.ok(studyRoomId);
    }

    @GetMapping("/{studyRoomId}/codes")
    public ResponseEntity<Integer> getConnectionCode(@PathVariable Long studyRoomId){
        Integer connectionCode = studyRoomService.getConnectionCode(studyRoomId);
        return ResponseEntity.ok(connectionCode);
    }
}
