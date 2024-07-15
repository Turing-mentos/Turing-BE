package turing.turing.domain.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.Provider;

@Getter
@AllArgsConstructor
public class TeacherSignUpRequest {

    private String email;
    private String name;
    private Provider provider;
}
