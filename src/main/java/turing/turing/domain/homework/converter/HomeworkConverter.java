package turing.turing.domain.homework.converter;

import turing.turing.domain.homework.Homework;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.notebook.Notebook;

public class HomeworkConverter {

    public static Homework toEntity (DetailedHomeworkDto dto, Notebook notebook) {
        return Homework.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .rangeType(dto.getRangeType())
                .rangeStart(dto.getRangeStart())
                .rangeEnd(dto.getRangeEnd())
                .content(dto.getContent())
                .memo(dto.getMemo())
                .notebook(notebook)
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
}
