package turing.turing.domain.studyRoom.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.studyTime.dto.StudyTimeResDto;

@Getter
@AllArgsConstructor
public class BaseTemplateDto {
    private Long studyRoomId;
    private List<StudyTimeResDto> studyTimeList;
    private Integer baseSession;
    private Integer wage;
}
