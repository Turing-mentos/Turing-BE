package turing.turing.domain.studyRoom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.studyRoom.dto.response.DetailedStudyRoomResDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomCreateReqDto;
import turing.turing.domain.studyRoom.dto.response.StudyRoomResDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomUpdateReqDto;
import turing.turing.domain.studyRoom.dto.response.SubjectAndTeacherResDto;

import java.util.List;

@Tag(name = "StudyRoom", description = "과외공간")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    // 선생님 ID 필요
    @Operation(summary = "과외 정보 등록")
    @PostMapping
    public ResponseEntity<Long> createStudyRoom(@RequestBody StudyRoomCreateReqDto studyRoomCreateReqDto){
        Long studyRoomId = studyRoomService.createStudyRoom(1L, studyRoomCreateReqDto);
        return ResponseEntity.ok(studyRoomId);
    }

    @Operation(summary = "과외 정보 수정")
    @PutMapping("/{studyRoomId}")
    public ResponseEntity<Void> updateStudyRoom(@PathVariable Long studyRoomId, @RequestBody StudyRoomUpdateReqDto studyRoomUpdateReqDto){
        studyRoomService.updateStudyRoom(studyRoomId, studyRoomUpdateReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "과외 정보 삭제")
    @DeleteMapping("/{studyRoomId}")
    public ResponseEntity<Void> deleteStudyRoom(@PathVariable Long studyRoomId){
        studyRoomService.deleteStudyRoom(studyRoomId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[선생님] 학생 연결 코드 조회 (코드 생성)")
    @GetMapping("/{studyRoomId}/codes")
    public ResponseEntity<Integer> getConnectionCode(@PathVariable Long studyRoomId){
        Integer code = studyRoomService.getConnectionCode(studyRoomId);
        return ResponseEntity.ok(code);
    }

    @Operation(summary = "[학생] 연결 코드를 통한 선생님 조회 (연결할 선생님 조회)")
    @GetMapping("/before-connect")
    public ResponseEntity<SubjectAndTeacherResDto> getTeacherByCode(@RequestParam(required = true) Integer code){
        SubjectAndTeacherResDto subjectAndTeacherResDto = studyRoomService.getTeacherByCode(code);
        return ResponseEntity.ok(subjectAndTeacherResDto);
    }

    // 학생 ID 필요
    @Operation(summary = "[학생] 선생님-학생 연결")
    @PatchMapping("/connect")
    public ResponseEntity<Void> connectTeacherStudent(@RequestParam(required = true) Integer code){
        studyRoomService.connectTeacherStudent(4L, code);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "선생님-학생 연결 해제")
    @PatchMapping("/{studyRoomId}/disconnect")
    public ResponseEntity<Void> disconnectTeacherStudent(@PathVariable Long studyRoomId){
        studyRoomService.disconnectTeacherStudent(studyRoomId);
        return ResponseEntity.ok().build();
    }

    // 학생 or 선생님 ID 필요 (+ role)
    @Operation(summary = "진행중인 수업 조회 (+연결 여부)")
    @GetMapping
    public ResponseEntity<List<StudyRoomResDto>> getStudyRooms(){
        List<StudyRoomResDto> studyRoomResDtoList = studyRoomService.getStudyRooms(1L);
        return ResponseEntity.ok(studyRoomResDtoList);
    }

    // 학생 or 선생님 ID 필요 (+ role)
    @Operation(summary = "진행중인 수업 상세 조회")
    @GetMapping("/{studyRoomId}")
    public ResponseEntity<DetailedStudyRoomResDto> getDetailedStudyRooms(@PathVariable Long studyRoomId){
        DetailedStudyRoomResDto detailedStudyRoomResDto = studyRoomService.getDetailedStudyRooms(studyRoomId);
        return ResponseEntity.ok(detailedStudyRoomResDto);
    }
}
