package turing.turing.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.report.dto.response.ReportReadAllDto;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
  
    @Query("SELECT r " +
            "FROM Report r " +
            "JOIN r.schedule s " +
            "JOIN s.studyRoom sr " +
            "JOIN sr.teacher t " +
            "WHERE t.id = :teacherId " +
            "AND r.id = :reportId")
    Report findReportByTeacherIdAndId(@Param("teacherId") Long teacherId, @Param("reportId") Long reportId);
   
    @Query("SELECT r.id as reportId, st.id as studentId, s.studentName as name, s.subject as subject, s.session as session, r.createdAt as createdAt, r.updatedAt as updatedAt " +
            "FROM Report r " +
            "JOIN r.schedule s " +
            "JOIN s.studyRoom sr " +
            "JOIN sr.teacher t " +
            "JOIN sr.student st " +
            "WHERE t.id = :teacherId")
    List<ReportReadAllDto> findAllReportsByTeacherId(@Param("teacherId") Long teacherId);
}
