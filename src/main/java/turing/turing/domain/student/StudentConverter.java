package turing.turing.domain.student;

import turing.turing.domain.student.dto.ProfileDto;

public class StudentConverter {
    public static ProfileDto toProfileDto(Student student){
        return ProfileDto.builder()
                .name(student.getName()).build();
    }
}

