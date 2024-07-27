package turing.turing.domain.homework.converter;

import turing.turing.domain.homework.Homework;
import turing.turing.domain.homework.dto.CreateHomeworkRequest;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.homework.dto.HomeworkDto;
import turing.turing.domain.notebook.Notebook;

public class HomeworkConverter {

    public static Homework toEntity (CreateHomeworkRequest request, Notebook notebook) {
        return Homework.builder()
                .category(request.getCategory())
                .title(request.getTitle())
                .rangeType(request.getRangeType())
                .rangeStart(request.getRangeStart())
                .rangeEnd(request.getRangeEnd())
                .content(request.getContent())
                .memo(request.getMemo())
                .notebook(notebook)
                .isDone(false)
                .build();
    }


    public static DetailedHomeworkDto toDetailedDto(Homework homework) {
        return DetailedHomeworkDto.builder()
                .homeworkId(homework.getId())
                .category(homework.getCategory())
                .title(homework.getTitle())
                .rangeType(homework.getRangeType())
                .rangeStart(homework.getRangeStart())
                .rangeEnd(homework.getRangeEnd())
                .content(homework.getContent())
                .memo(homework.getMemo())
                .notebookId(homework.getNotebook().getId())
                .build();
    }

    public static HomeworkDto toDto(Homework homework) {
        return HomeworkDto.builder()
                .homeworkId(homework.getId())
                .category(homework.getCategory())
                .title(homework.getTitle())
                .rangeType(homework.getRangeType())
                .rangeStart(homework.getRangeStart())
                .rangeEnd(homework.getRangeEnd())
                .content(homework.getContent())
                .memo(homework.getMemo())
                .isDone(homework.getIsDone())
                .build();
    }
}
