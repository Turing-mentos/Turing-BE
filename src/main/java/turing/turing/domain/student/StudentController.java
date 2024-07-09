package turing.turing.domain.student;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.student.dto.ProfileDto;

@RestController
@AllArgsConstructor

public class StudentController {

    private final StudentService studentService;

//    @PatchMapping("api/profile/student")
//    public ResponseEntity<String> updateProfile(@RequestBody ProfileDto profileDto){
//        //예시 spring context에서 멤버 정도 얻어옴
//        Long memberId = 1L;
//        studentService.updateProfile(profileDto, memberId);
//        return ResponseEntity.ok(null);
//    }

//    @PatchMapping("api/profile/student")
//    public ResponseEntity<String> updateProfile(@RequestBody ProfileDto profileDto) {
//        //예시 spring context에서 멤버 정도 얻어옴
//        Long memberId = 1L;
//        studentService.readProfile(memberId);
//        return ResponseEntity.ok(null);
//    }
    @GetMapping("api/profile/student")
    public ResponseEntity<ProfileDto> readProfile(){
        //예시 spring context에서 멤버 정도 얻어옴
        Long memberId = 1L;
        return ResponseEntity.ok(studentService.readProfile(memberId));
    }
}
