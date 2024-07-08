package turing.turing.domain.teacher;

import turing.turing.domain.teacher.dto.ProfileDto;

public class TeacherConverter {

    public static ProfileDto toProfileDto(Teacher teacher){
        return ProfileDto.builder()
                .department(teacher.getDepartment())
                .studentNumber(teacher.getStudentNumber())
                .university(teacher.getUniversity())
                .name(teacher.getName())
                .build();
    }
}
