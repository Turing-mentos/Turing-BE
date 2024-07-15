package turing.turing.domain.studyRoom.dto;

import turing.turing.domain.studyRoom.StudyRoom;

public record StudyRoomResDto(
        Long id,
        String studentName,
        String subject,
        Boolean linkStatus
) {
    public static StudyRoomResDto of(StudyRoom studyRoom) {
        return new StudyRoomResDto(
                studyRoom.getId(),
                studyRoom.getStudent().getName(),
                studyRoom.getSubject(),
                studyRoom.getLinkStatus()
        );
    }
}
