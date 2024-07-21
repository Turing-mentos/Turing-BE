package turing.turing.domain.studyRoom.dto.response;

import turing.turing.domain.member.Role;
import turing.turing.domain.studyRoom.StudyRoom;

public record StudyRoomResDto(
        Long id,
        String opponentFirstName,
        String opponentLastName,
        String subject,
        Boolean linkStatus
) {
    public static StudyRoomResDto of(StudyRoom studyRoom, Role role) {
        return new StudyRoomResDto(
                studyRoom.getId(),
                role == Role.TEACHER ? studyRoom.getStudent().getFirstName() : studyRoom.getTeacher().getFirstName(),
                role == Role.TEACHER ? studyRoom.getStudent().getLastName() : studyRoom.getTeacher().getLastName(),
                studyRoom.getSubject(),
                studyRoom.getLinkStatus()
        );
    }
}
