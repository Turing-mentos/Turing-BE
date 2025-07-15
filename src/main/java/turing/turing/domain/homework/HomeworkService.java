package turing.turing.domain.homework;

import turing.turing.domain.homework.dto.CreateHomeworkRequest;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.homework.dto.UpdateHomeworkRequest;

public interface HomeworkService {

    DetailedHomeworkDto getHomework(Long homeworkId);

    Long createHomework(CreateHomeworkRequest request);

    Long updateHomework(UpdateHomeworkRequest request);

    Long updateDone(Long homeworkId);

    void deleteHomework(Long homeworkId);
}
