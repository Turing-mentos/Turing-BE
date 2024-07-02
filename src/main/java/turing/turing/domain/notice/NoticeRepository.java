package turing.turing.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.notebook.Notebook;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> searchNoticeByReceiverIdAndReceiverRoleAndReadStatus(Long recevierId, String receiverRole, boolean readStatus);
    List<Notice> findAllByReceiverIdAndRecAndReceiverRole(Long receiverId, String receiverRole);
}
