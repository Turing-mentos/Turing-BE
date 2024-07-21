package turing.turing.domain.student;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.member.Provider;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByEmailAndProvider(String email, Provider provider);
}
