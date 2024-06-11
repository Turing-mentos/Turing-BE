package turing.turing.domain.homework;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.notebook.Notebook;
import turing.turing.domain.notebook.NotebookRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final NotebookRepository notebookRepository;

    @Transactional(readOnly = true)
    public DetailedHomeworkDto getHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        return homework.toDetailedDto(homeworkId);
    }

    public Long createHomework(DetailedHomeworkDto request) {
        Notebook notebook = notebookRepository.findById(request.getNotebookId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Homework homework = homeworkRepository.save(request.toEntity(notebook));

        return homework.getId();
    }

    public Long updateHomework(DetailedHomeworkDto request) {
        Homework homework = homeworkRepository.findById(request.getHomeworkId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        return homework.update(request);
    }

    public void deleteHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        homeworkRepository.delete(homework);
    }
}
