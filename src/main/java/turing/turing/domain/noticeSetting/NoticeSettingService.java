package turing.turing.domain.noticeSetting;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@AllArgsConstructor
public class NoticeSettingService {

    private final NoticeSettingRepository noticeSettingRepository;
    public void changeSetting(Long notificationSettingId) {
        NoticeSetting noticeSetting = noticeSettingRepository.findById(notificationSettingId)
                .orElseThrow(()-> new RestApiException(CommonErrorCode.NOT_FOUND));
        noticeSetting.changeEnabled(!noticeSetting.getEnabled());
        noticeSettingRepository.save(noticeSetting);
    }
}
