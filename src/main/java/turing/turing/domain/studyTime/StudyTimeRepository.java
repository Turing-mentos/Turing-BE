package turing.turing.domain.studyTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    // 벌크 연산 활용
    @Modifying
    @Query("DELETE FROM StudyTime st WHERE st.studyRoom.id = :studyRoomId")
    void deleteByStudyRoomId(@Param("studyRoomId") Long studyRoomId);
}
