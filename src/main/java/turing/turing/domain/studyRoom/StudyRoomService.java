package turing.turing.domain.studyRoom;

import turing.turing.domain.member.Role;
import turing.turing.domain.studyRoom.dto.BaseTemplateDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomCreateReqDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomUpdateReqDto;
import turing.turing.domain.studyRoom.dto.response.DetailedStudyRoomResDto;
import turing.turing.domain.studyRoom.dto.response.StudyRoomResDto;
import turing.turing.domain.studyRoom.dto.response.SubjectAndTeacherResDto;

import java.util.List;

public interface StudyRoomService {

    Long createStudyRoom(Long teacherId, StudyRoomCreateReqDto studyRoomCreateReqDto);

    void updateStudyRoom(Long studyRoomId, StudyRoomUpdateReqDto studyRoomUpdateReqDto);

    void deleteStudyRoom(Long studyRoomId);

    Integer getConnectionCode(Long studyRoomId);

    SubjectAndTeacherResDto getTeacherByCode(Integer code);

    void connectTeacherStudent(Long studentId, Integer code);

    void disconnectTeacherStudent(Long studyRoomId);

    List<StudyRoomResDto> getStudyRooms(Role role, Long memberId);

    DetailedStudyRoomResDto getDetailedStudyRoom(Long studyRoomId, Role role);

    Integer generateCode();

    BaseTemplateDto getBaseTemplate(Long studyRoomId);

}