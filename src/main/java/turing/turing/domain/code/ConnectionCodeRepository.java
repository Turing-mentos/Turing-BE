package turing.turing.domain.code;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.Optional;

public interface ConnectionCodeRepository extends JpaRepository<ConnectionCode, Long> {
    Optional<ConnectionCode> findByStudyRoom(StudyRoom studyRoom);
    Boolean existsByConnectionCode(Integer connectionCode);
}
