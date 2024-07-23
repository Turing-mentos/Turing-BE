package turing.turing.domain.notebook.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@Getter
@SuperBuilder
public class CreateNotebookResponse extends BaseNoticeInfo {

    private Long notebookId;
}
