package turing.turing.domain.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.Optional;

public interface ConnectionCodeRepository extends JpaRepository<ConnectionCode, Long> {

    Optional<ConnectionCode> findByStudyRoom(StudyRoom studyRoom);

    Boolean existsByCode(Integer code);

    @Query("select c from ConnectionCode c join fetch c.studyRoom s where c.code = :code")
    Optional<ConnectionCode> findWithStudyRoomByCode(@Param(value = "code") Integer code);


}
