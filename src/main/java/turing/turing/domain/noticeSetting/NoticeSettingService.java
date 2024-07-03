package turing.turing.domain.noticeSetting;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import turing.turing.domain.noticeSetting.dto.NoticeSettingDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;

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

    public List<NoticeSettingDto.ResponseDto> readSetting(Long memberId, String memberRole) {
        List<NoticeSetting> noticeSettingList = noticeSettingRepository.findAllByMemberIdAndRole(memberId, memberRole);
        return NoticeSettingConverter.toDtoList(noticeSettingList);
    }
}
