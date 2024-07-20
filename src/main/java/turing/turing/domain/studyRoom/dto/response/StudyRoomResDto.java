package turing.turing.domain.studyRoom.dto.response;

import turing.turing.domain.studyRoom.StudyRoom;

public record StudyRoomResDto(
        Long id,
        String studentFirstName,
        String studentLastName,
        String subject,
        Boolean linkStatus
) {
    public static StudyRoomResDto of(StudyRoom studyRoom) {
        return new StudyRoomResDto(
                studyRoom.getId(),
                studyRoom.getStudent().getFirstName(),
                studyRoom.getStudent().getLastName(),
                studyRoom.getSubject(),
                studyRoom.getLinkStatus()
        );
    }
}
