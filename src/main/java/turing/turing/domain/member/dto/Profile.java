package turing.turing.domain.member.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class Profile {
    private String firstName;
    private String lastName;
    private String university;
    private String department;
    private String studentNumber;
}
