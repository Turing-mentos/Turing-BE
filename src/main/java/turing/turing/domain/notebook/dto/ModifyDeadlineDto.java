package turing.turing.domain.notebook.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyDeadlineDto {

    private Long notebookId;
    private LocalDate deadline;
}
