package turing.turing.domain.notebook.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CheckLatestRequest {

    @NotEmpty
    private Long studyRoomId;
}
