package turing.turing.domain.student;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.member.Provider;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByEmailAndProvider(String email, Provider provider);

    @Query("select s.id from Student s "
            + "join StudyRoom sr on s.id = sr.student.id "
            + "join Schedule sd on sr.id = sd.studyRoom.id "
            + "where sd.id = :scheduleId")
    Optional<Long> findByScheduleId(@Param("scheduleId") Long scheduleId) ;
}
