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

    // 선생님 ID 필요
    @PostMapping
    public ResponseEntity<Long> createStudyRoom(@RequestBody StudyRoomReqDto studyRoomReqDto){
        Long studyRoomId = studyRoomService.createStudyRoom(1L, studyRoomReqDto);
        return ResponseEntity.ok(studyRoomId);
    }

    @GetMapping("/{studyRoomId}/codes")
    public ResponseEntity<Integer> getConnectionCode(@PathVariable Long studyRoomId){
        Integer code = studyRoomService.getConnectionCode(studyRoomId);
        return ResponseEntity.ok(code);
    }

    // 학생 ID 필요
    @PatchMapping("/connect")
    public ResponseEntity<Void> connectTeacherStudent(@RequestParam(required = true) Integer code){
        studyRoomService.connectTeacherStudent(4L, code);
        return ResponseEntity.ok().build();
    }

}
