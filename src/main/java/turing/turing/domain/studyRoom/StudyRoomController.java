package turing.turing.domain.studyRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.studyRoom.dto.DetailedStudyRoomResDto;
import turing.turing.domain.studyRoom.dto.StudyRoomCreateReqDto;
import turing.turing.domain.studyRoom.dto.StudyRoomResDto;
import turing.turing.domain.studyRoom.dto.StudyRoomUpdateReqDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    // 선생님 ID 필요
    @PostMapping
    public ResponseEntity<Long> createStudyRoom(@RequestBody StudyRoomCreateReqDto studyRoomCreateReqDto){
        Long studyRoomId = studyRoomService.createStudyRoom(1L, studyRoomCreateReqDto);
        return ResponseEntity.ok(studyRoomId);
    }

    @PutMapping("/{studyRoomId}")
    public ResponseEntity<Void> updateStudyRoom(@PathVariable Long studyRoomId, @RequestBody StudyRoomUpdateReqDto studyRoomUpdateReqDto){
        studyRoomService.updateStudyRoom(studyRoomId, studyRoomUpdateReqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studyRoomId}")
    public ResponseEntity<Void> deleteStudyRoom(@PathVariable Long studyRoomId){
        studyRoomService.deleteStudyRoom(studyRoomId);
        return ResponseEntity.ok().build();
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

    @PatchMapping("/{studyRoomId}/disconnect")
    public ResponseEntity<Void> disconnectTeacherStudent(@PathVariable Long studyRoomId){
        studyRoomService.disconnectTeacherStudent(studyRoomId);
        return ResponseEntity.ok().build();
    }

    // 학생 or 선생님 ID 필요 (+ role)
    @GetMapping
    public ResponseEntity<List<StudyRoomResDto>> getStudyRooms(){
        List<StudyRoomResDto> studyRoomResDtoList = studyRoomService.getStudyRooms(1L);
        return ResponseEntity.ok(studyRoomResDtoList);
    }

    // 학생 or 선생님 ID 필요 (+ role)
    @GetMapping("/{studyRoomId}")
    public ResponseEntity<DetailedStudyRoomResDto> getDetailedStudyRooms(@PathVariable Long studyRoomId){
        DetailedStudyRoomResDto detailedStudyRoomResDto = studyRoomService.getDetailedStudyRooms(studyRoomId);
        return ResponseEntity.ok(detailedStudyRoomResDto);
    }
}
