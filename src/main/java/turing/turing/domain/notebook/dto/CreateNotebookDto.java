package turing.turing.domain.notebook.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateNotebookDto {

    private Long scheduleId;

    private LocalDate deadline;
}
