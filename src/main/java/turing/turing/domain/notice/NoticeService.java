package turing.turing.domain.notice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;

@Service
@AllArgsConstructor
public class NoticeService {

    private  final NoticeRepository noticeRepository ;
    public void checkNotification(Long notificationId) {
        Notice notice = noticeRepository.findById(notificationId).orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));
        notice.updateRead(true);

        noticeRepository.save(notice);
    }


    public int unCheckedNotification() {
        //멤버 아이디 가져와서
        List<Notice> noticeList = noticeRepository.searchNoticeByReceiverIdAndReceiverRoleAndReadStatus(id,"TEACHER", false);

        return noticeList.size();
    }
}
