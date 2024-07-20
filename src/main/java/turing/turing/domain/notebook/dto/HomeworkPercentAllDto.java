package turing.turing.domain.notebook.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface HomeworkPercentAllDto {

    Long getId();

    String getName();

    String getFirstName();
    String getLastName();

    String getSubject();
    int getCompletionPercent();
}
