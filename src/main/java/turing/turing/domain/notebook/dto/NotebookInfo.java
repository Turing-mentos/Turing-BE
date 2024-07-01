package turing.turing.domain.notebook.dto;

import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.homework.dto.HomeworkDto;

@Getter
@Builder
public class NotebookInfo {

    private Long notebookId;
    private String studentName;
    private String subject;
    private Timestamp deadline;
    private Boolean isDone;
    private List<HomeworkDto> homeworkDtoList;

    public void updateDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
}
