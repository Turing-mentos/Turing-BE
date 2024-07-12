package turing.turing.domain.notice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import turing.turing.domain.notice.converter.NoticeConverter;
import turing.turing.domain.notice.dto.NoticeDto;
import turing.turing.domain.teacher.Teacher;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NoticeService {

    private  final NoticeRepository noticeRepository ;
    public void checkNotification(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));
        notice.updateRead(true);

        noticeRepository.save(notice);
    }


    public int unCheckedNotification() {
        //멤버 아이디 가져와서 이부분은 추후 추라 @Authen~ 어노테이션 컨트롤러에서 써야함
        List<Notice> noticeList = noticeRepository.findAllByReceiverIdAndReceiverRoleAndReadStatus(1L,"TEACHER", false);
        //List<Notice> noticeList = noticeRepository.searchNoticeByReceiverIdAndReceiverRoleAndReadStatus(id,"STUDENT", false);
        return noticeList.size();

    }

    public List<NoticeDto.ResponseDto> readAllNotification(Long memberId, String memberRole) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        List<Notice> noticeList = noticeRepository.findAllByReceiverIdAndReceiverRoleAndCreatedAtAfter(memberId, memberRole, oneMonthAgo);
        if (noticeList.isEmpty()) {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
        return NoticeConverter.toDtoList(noticeList);

    }

    public NoticeDto.ResponseForNotice remindNoteBook(NoticeDto.RemindNoteBookDto remindNoteBookDto, Teacher teacher) {
        Long teacherId = 1L;
        return NoticeDto.ResponseForNotice.builder().
            receiverId(remindNoteBookDto.getStudentId())
                .senderId(teacherId)
                .build();
    }
}
