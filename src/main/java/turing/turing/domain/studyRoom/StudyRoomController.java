package turing.turing.domain.studyRoom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;
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

    @Operation(summary = "[선생님] 과외 정보 등록")
    @PostMapping
    public ResponseEntity<Long> createStudyRoom(@AuthenticationPrincipal CustomUserDetails user, @RequestBody StudyRoomCreateReqDto studyRoomCreateReqDto){
        Long teacherId = user.getMemberId();
        Long studyRoomId = studyRoomService.createStudyRoom(teacherId, studyRoomCreateReqDto);
        return ResponseEntity.ok(studyRoomId);
    }

    @Operation(summary = "[선생님] 과외 정보 수정")
    @PutMapping("/{studyRoomId}")
    public ResponseEntity<Void> updateStudyRoom(@PathVariable Long studyRoomId, @RequestBody StudyRoomUpdateReqDto studyRoomUpdateReqDto){
        studyRoomService.updateStudyRoom(studyRoomId, studyRoomUpdateReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[선생님] 과외 정보 삭제")
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

    @Operation(summary = "[학생] 선생님-학생 연결")
    @PatchMapping("/connect")
    public ResponseEntity<Void> connectTeacherStudent(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(required = true) Integer code){
        Long studentId = user.getMemberId();
        studyRoomService.connectTeacherStudent(studentId, code);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "선생님-학생 연결 해제")
    @PatchMapping("/{studyRoomId}/disconnect")
    public ResponseEntity<Void> disconnectTeacherStudent(@PathVariable Long studyRoomId){
        studyRoomService.disconnectTeacherStudent(studyRoomId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "진행중인 수업 조회 (+연결 여부)")
    @GetMapping
    public ResponseEntity<List<StudyRoomResDto>> getStudyRooms(@AuthenticationPrincipal CustomUserDetails user){
        Role role = user.getRole();
        Long memberId = user.getMemberId();
        List<StudyRoomResDto> studyRoomResDtoList = studyRoomService.getStudyRooms(role, memberId);
        return ResponseEntity.ok(studyRoomResDtoList);
    }

    @Operation(summary = "진행중인 수업 상세 조회")
    @GetMapping("/{studyRoomId}")
    public ResponseEntity<DetailedStudyRoomResDto> getDetailedStudyRooms(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long studyRoomId){
        Role role = user.getRole();
        DetailedStudyRoomResDto detailedStudyRoomResDto = studyRoomService.getDetailedStudyRooms(studyRoomId, role);
        return ResponseEntity.ok(detailedStudyRoomResDto);
    }
}
