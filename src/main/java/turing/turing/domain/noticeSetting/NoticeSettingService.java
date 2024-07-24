package turing.turing.domain.noticeSetting;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;
import turing.turing.domain.noticeSetting.dto.NoticeSettingDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.exception.errorCode.NotificationError;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NoticeSettingService {

    private final NoticeSettingRepository noticeSettingRepository;
    public boolean changeSetting(CustomUserDetails userDetails, Long notificationSettingId) {
        NoticeSetting noticeSetting = noticeSettingRepository.findByMemberIdAndRoleAndId(userDetails.getMemberId(), String.valueOf(userDetails.getRole()), notificationSettingId);
        if(noticeSetting == null){
            throw new RestApiException(CommonErrorCode.NOT_FOUND);
        }
        noticeSetting.changeEnabled(!noticeSetting.getEnabled());
        noticeSettingRepository.save(noticeSetting);

        return  noticeSetting.getEnabled();
    }

    public List<NoticeSettingDto.ResponseDto> readSetting(CustomUserDetails userDetails) {
        List<NoticeSetting> noticeSettingList = noticeSettingRepository.findAllByMemberIdAndRole(userDetails.getMemberId(), String.valueOf(userDetails.getRole()));

        return NoticeSettingConverter.toDtoList(noticeSettingList);
    }

    //알림 설정 생성(회원가입 시 최초 한번만)
    public void createSetting(CustomUserDetails userDetails, boolean enabled) {

        final String[] teacherCategories = {"NOTEBOOK", "HOMEWORK", "COMMENT", "QUESTION", "SCHEDULE_CHANGE", "NEW_SCHEDULE", "REPORT", "SESSION"};
        final String[] studentCategories = {"NOTEBOOK", "HOMEWORK", "SCHEDULE_CHANGE", "COMMENT"};


        List<NoticeSetting> checkSetting = noticeSettingRepository.findAllByMemberIdAndRole(userDetails.getMemberId(), String.valueOf(userDetails.getRole()));
        if(checkSetting != null){
            throw new RestApiException(NotificationError.NOTIFICATION_SETTING_FAILURE);
        }

        if (userDetails.getRole() == Role.TEACHER) {


            List<NoticeSetting> teacherNoticeSettings = new ArrayList<>();
            for (String category : teacherCategories) {
                NoticeSetting noticeSetting = NoticeSetting.builder()
                        .memberId(userDetails.getMemberId())
                        .role(String.valueOf(userDetails.getRole()))
                        .enabled(enabled)
                        .category(category)
                        .build();
                teacherNoticeSettings.add(noticeSetting);
            }
            noticeSettingRepository.saveAll(teacherNoticeSettings);
        }
        else if(userDetails.getRole() == Role.STUDENT){
            List<NoticeSetting> studentNoticeSettings = new ArrayList<>();
            for (String category : studentCategories) {
                NoticeSetting noticeSetting = NoticeSetting.builder()
                        .memberId(userDetails.getMemberId())
                        .role(String.valueOf(userDetails.getRole()))
                        .enabled(enabled)
                        .category(category)
                        .build();
                studentNoticeSettings.add(noticeSetting);
            }
            noticeSettingRepository.saveAll(studentNoticeSettings);
        }
    }
}
