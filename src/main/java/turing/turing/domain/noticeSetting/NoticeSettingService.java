package turing.turing.domain.noticeSetting;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import turing.turing.domain.noticeSetting.dto.NoticeSettingDto;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.ArrayList;
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

    //알림 설정 생성(회원가입 시 최초 한번만)
    public void createSetting(Long memberId, String memberRole, boolean enabled) {

        final String[] teacherCategories = {"NOTEBOOK", "HOMEWORK", "COMMENT", "QUESTION", "SCHEDULE_CHANGE", "NEW_SCHEDULE", "REPORT", "SESSION"};
        final String[] studentCategories = {"NOTEBOOK", "HOMEWORK", "SCHEDULE_CHANGE", "COMMENT"};
        if (memberRole.equals("TEACHER")) {
            List<NoticeSetting> teacherNoticeSettings = new ArrayList<>();
            for (String category : teacherCategories) {
                NoticeSetting noticeSetting = NoticeSetting.builder()
                        .memberId(memberId)
                        .role(memberRole)
                        .enabled(enabled)
                        .category(category)
                        .build();
                teacherNoticeSettings.add(noticeSetting);
            }
            noticeSettingRepository.saveAll(teacherNoticeSettings);
        }
        else if(memberRole.equals("STUDENT")){
            List<NoticeSetting> studentNoticeSettings = new ArrayList<>();
            for (String category : studentCategories) {
                NoticeSetting noticeSetting = NoticeSetting.builder()
                        .memberId(memberId)
                        .role(memberRole)
                        .enabled(enabled)
                        .category(category)
                        .build();
                studentNoticeSettings.add(noticeSetting);
            }
            noticeSettingRepository.saveAll(studentNoticeSettings);
        }
    }
}
