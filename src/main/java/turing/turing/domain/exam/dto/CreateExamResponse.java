package turing.turing.domain.exam.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@Getter
@SuperBuilder
public class CreateExamResponse extends BaseNoticeInfo {
    private Long examId;

    public void setExamId(Long examId) {
        this.examId = examId;
    }
}
