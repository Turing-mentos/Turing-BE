package turing.turing.domain.studyRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
}
