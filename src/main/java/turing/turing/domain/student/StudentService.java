package turing.turing.domain.student;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import turing.turing.domain.student.dto.ProfileDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;


    public void updateProfile(ProfileDto profileDto, Long memberId) {
        Student s = studentRepository.findById(memberId).orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));
        s.updateProfile(profileDto);
    }


    public ProfileDto readProfile(Long memberId) {
        Student s = studentRepository.findById(memberId).orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));

        return StudentConverter.toProfileDto(s);
    }
}
