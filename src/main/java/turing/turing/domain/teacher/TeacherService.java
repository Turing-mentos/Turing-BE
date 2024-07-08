package turing.turing.domain.teacher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.teacher.dto.ProfileDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public ProfileDto readProfile(Long teacherId) {

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));

        return TeacherConverter.toProfileDto(teacher);
    }


    @Transactional
    public ProfileDto updateProfile(Long teacherId, ProfileDto profileDto) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));

        // 프로필을 처음 업데이트하는 경우 (createdAt과 updatedAt이 동일한 경우)
        if (teacher.getCreatedAt().equals(teacher.getUpdatedAt())) {
            teacher.updateProfile(profileDto);
            return profileDto;
        }

        // 마지막 업데이트 시간과 현재 시간을 비교하여 1년이 지났는지 확인합니다.
        Timestamp oneYearAgo = Timestamp.valueOf(LocalDateTime.now().minusYears(1));

        if (teacher.getUpdatedAt().before(oneYearAgo)) {
            teacher.updateProfile(profileDto);
            return profileDto;
        } else {
            throw new RestApiException(CommonErrorCode.UPDATE_NOT_ALLOWED);
        }
    }
}
