package turing.turing.domain.teacher.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileDto {

    private String name;
    private String university;
    private String department;

    private String studentNumber;
}
