package turing.turing.domain.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@Getter
@SuperBuilder
public class QuestionCreateResDto extends BaseNoticeInfo {
    private Long questionId;
    // 아래는 알림을 위해 필요한 필드
    private String category;
}
