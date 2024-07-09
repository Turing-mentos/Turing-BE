package turing.turing.domain.teacher;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.teacher.dto.ProfileDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TeacherController {

    private final TeacherService teacherService;
    
    @GetMapping("/teacher/profile")
    public ResponseEntity<ProfileDto> readProfile(){
        //@Authen~~로 얻어오기, 밑에는 예시
        Long teacherId = 1L;
        
        return ResponseEntity.ok(teacherService.readProfile(teacherId));
    }

    @PatchMapping("/teacher/profile")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody ProfileDto profileDto){
        //추후 학생 마이페이지와 분리 필요
        //@Authen~~로 얻어오기, 밑에는 예시
        Long teacherId = 1L;

        return ResponseEntity.ok(teacherService.updateProfile(teacherId, profileDto));
    }
}
