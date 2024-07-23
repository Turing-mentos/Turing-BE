package turing.turing.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.notebook.Notebook;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByReceiverIdAndReceiverRoleAndReadStatus(Long receiverId, String receiverRole, boolean readStatus);
    List<Notice> findAllByReceiverIdAndReceiverRoleAndCreatedAtAfter(Long receiverId, String receiverRole, LocalDateTime createdAt);

    @Query("SELECT n FROM Notice n WHERE n.id = :noticeId AND n.receiverId = :memberId")
    Notice findByIdAndReceiverId(@Param("noticeId") Long noticeId, @Param("memberId") Long memberId);
}
