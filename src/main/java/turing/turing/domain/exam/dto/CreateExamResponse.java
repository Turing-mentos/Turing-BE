package turing.turing.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@Getter
@Builder
public class CreateExamResponse {

    private Long examId;
    private BaseNoticeInfo baseNoticeInfo;
}
