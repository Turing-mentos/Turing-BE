package turing.turing.domain.notice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;
import turing.turing.domain.notice.converter.NoticeConverter;
import turing.turing.domain.notice.dto.NoticeDto;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.exception.errorCode.NotificationError;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NoticeService {

    private  final NoticeRepository noticeRepository ;
    private  final StudyRoomRepository studyRoomRepository ;
    public void checkNotification(CustomUserDetails userDetail, Long noticeId) {

        Notice notice = noticeRepository.findByIdAndReceiverId(noticeId, userDetail.getMemberId());
        if(notice==null)
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        notice.updateRead(true);

        noticeRepository.save(notice);
    }


    public int unCheckedNotification(CustomUserDetails userDetails) {

        List<Notice> noticeList = noticeRepository.findAllByReceiverIdAndReceiverRoleAndReadStatus(userDetails.getMemberId(), String.valueOf(userDetails.getRole()), false);

        return noticeList.size();

    }

    public List<NoticeDto.ResponseDto> readAllNotification(CustomUserDetails userDetails) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        List<Notice> noticeList = noticeRepository.findAllByReceiverIdAndReceiverRoleAndCreatedAtAfter(userDetails.getMemberId(), String.valueOf(userDetails.getRole()), oneMonthAgo);
        if (noticeList.isEmpty()) {
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
        return NoticeConverter.toDtoList(noticeList);

    }

    public NoticeDto.ResponseForNotice remindNoteBook(CustomUserDetails userDetails, Long studentId) {

        StudyRoom studyRoom = studyRoomRepository.findByTeacherIdAndStudentId(userDetails.getMemberId(), studentId);
        if(studyRoom == null){
            throw new RestApiException(NotificationError.NOTIFICATION_STUDYROOM_NOT_FOUND);
        }
        if(userDetails.getRole() == Role.STUDENT){
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
        NoticeDto.ResponseForNotice notice = NoticeDto.ResponseForNotice.builder().
                receiverId(studentId)
                .senderId(userDetails.getMemberId())
                .build();
        return notice;
    }
}
