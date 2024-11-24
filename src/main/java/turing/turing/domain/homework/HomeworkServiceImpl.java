package turing.turing.domain.homework;

import static turing.turing.global.exception.errorCode.HomeworkErrorCode.*;
import static turing.turing.global.exception.errorCode.NotebookErrorCode.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.converter.HomeworkConverter;
import turing.turing.domain.homework.dto.CreateHomeworkRequest;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.homework.dto.UpdateHomeworkRequest;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notebook.NotebookRepository;
import turing.turing.global.exception.RestApiException;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final NotebookRepository notebookRepository;

    @Transactional(readOnly = true)
    public DetailedHomeworkDto getHomework(Long homeworkId) {
        Homework homework = findById(homeworkId);

        return HomeworkConverter.toDetailedDto(homework);
    }

    public Long createHomework(CreateHomeworkRequest request) {
        Notebook notebook = notebookRepository.findById(request.getNotebookId())
                .orElseThrow(() -> new RestApiException(NOTEBOOK_NOT_FOUND));

        Homework homework = homeworkRepository.save(HomeworkConverter.toEntity(request, notebook));

        return homework.getId();
    }

    public Long updateHomework(UpdateHomeworkRequest request) {
        Homework homework = findById(request.getHomeworkId());

        return homework.update(request);
    }

    public Long updateDone(Long homeworkId) {
        Homework homework = findById(homeworkId);

        Boolean nowDone = homework.getIsDone();

        return homework.updateDone(!nowDone);
    }

    public void deleteHomework(Long homeworkId) {
        Homework homework = findById(homeworkId);

        homeworkRepository.delete(homework);
    }

    private Homework findById(Long homeworkId) {
        return homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RestApiException(HOMEWORK_NOT_FOUND));
    }
}
