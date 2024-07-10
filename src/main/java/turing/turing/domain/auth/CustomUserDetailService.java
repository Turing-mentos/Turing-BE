package turing.turing.domain.auth;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Object member = findByEmail(email);

        return new CustomUserDetails(email);
    }

    private Object findByEmail(String email) {
        Object member = teacherRepository.findByEmail(email);
        return Objects.requireNonNullElseGet(member, () -> studentRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND)));
    }
}
