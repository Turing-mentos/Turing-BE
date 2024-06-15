package turing.turing.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByStudyRoom(StudyRoom studyRoom);
}
