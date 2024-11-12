package turing.turing.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@SuperBuilder
@Getter
public class CommentCreateResDto extends BaseNoticeInfo {
    Long commentId;

    Long questionId;
}
