package turing.turing.domain.notebook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckLatestResponse {

    private Long scheduleId;
    private Boolean isExist;
}
