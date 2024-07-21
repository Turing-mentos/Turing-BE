package turing.turing.domain.teacher;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.member.Provider;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByEmailAndProvider(String email, Provider provider);

}
