package turing.turing.domain.notebook;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.homework.Homework;
import turing.turing.domain.homework.HomeworkRepository;
import turing.turing.domain.homework.converter.HomeworkConverter;
import turing.turing.domain.homework.dto.HomeworkDto;
import turing.turing.domain.notebook.dto.CreateNotebookDto;
import turing.turing.domain.notebook.dto.ModifyDeadlineDto;
import turing.turing.domain.notebook.dto.NotebookInfo;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final HomeworkRepository homeworkRepository;
    private final NotebookRepository notebookRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public NotebookInfo getNotebook(Long notebookId, Boolean b) {
        Notebook notebook = notebookRepository.findById(notebookId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Schedule schedule = notebook.getSchedule();

        List<Homework> homeworkList = homeworkRepository.findAllByNotebookId(notebookId);

        boolean isDone = true;
        List<HomeworkDto> homeworkDtoList = new ArrayList<>();
        for (Homework homework : homeworkList) {
            homeworkDtoList.add(HomeworkConverter.toDto(homework));
            if (isDone & !homework.getIsDone()) {
                isDone = false;
            }
        }

        NotebookInfo notebookInfo = NotebookInfo.builder()
                .notebookId(notebookId)
                .studentName(schedule.getStudentName())
                .subject(schedule.getSubject())
                .deadline(notebook.getDeadline())
                .isDone(isDone)
                .homeworkDtoList(homeworkDtoList)
                .build();

        if (b) {
            //TODO 등록되지 않은 회차 예측
            LocalDateTime nextSchedule = scheduleRepository.findNextDateById(schedule.getId(),
                    PageRequest.of(0, 1))
                    .atStartOfDay();
            notebookInfo.updateDeadline(Timestamp.valueOf(nextSchedule));
        }
        return notebookInfo;
    }

    @Transactional(readOnly = true)
    public List<NotebookInfo> getPastNotebooks(Long studyRoomId, Long notebookId) {
        PageRequest pageRequest = PageRequest.of(0, 11, Sort.by(Direction.DESC, "id"));

        List<Notebook> notebookList = notebookRepository.findAllByStudyRoomId(studyRoomId, notebookId, pageRequest);
        Sort sort = Sort.by(Direction.DESC, "notebook.id");

        return makeNotebookInfoList(notebookList, sort);
    }

    @Transactional(readOnly = true)
    public List<NotebookInfo> getThisWeekNotebook(List<Long> studyRoomIds) {
        List<Notebook> notebookList = notebookRepository.findAllByStudyRoomIds(studyRoomIds);
        Sort sort = Sort.by(Direction.ASC, "notebook.id");

        return makeNotebookInfoList(notebookList, sort);
    }

    @Transactional
    public Long createNotebook(CreateNotebookDto request) {
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Long studyRoomId = schedule.getStudyRoom().getId();
        LocalTime deadlineTime = scheduleRepository.findScheduleByDateAndStudyRoom(
                request.getDeadline(), studyRoomId);

        Timestamp timestamp = confirmDeadline(request.getDeadline(), deadlineTime);

        Notebook notebook = new Notebook(schedule, timestamp);
        notebookRepository.save(notebook);

        return notebook.getId();
    }

    @Transactional
    public Long modifyDeadline(ModifyDeadlineDto request) {
        Notebook notebook = notebookRepository.findById(request.getNotebookId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        LocalTime deadlineTime = scheduleRepository.findScheduleByDateAndNotebook(
                request.getDeadline(), request.getNotebookId());

        Timestamp timestamp = confirmDeadline(request.getDeadline(), deadlineTime);

        return notebook.modifyDeadline(timestamp);
    }

    @Transactional
    public void deleteNotebook(Long notebookId) {
        Notebook notebook = notebookRepository.findById(notebookId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        notebookRepository.delete(notebook);
    }

    private Timestamp confirmDeadline(LocalDate deadlineDate, LocalTime deadlineTime) {
        Timestamp timestamp;
        if (deadlineTime == null) {
            timestamp = Timestamp.valueOf(deadlineDate.atStartOfDay());
        } else {
            LocalDateTime localDateTime = LocalDateTime.of(deadlineDate, deadlineTime);
            timestamp = Timestamp.valueOf(localDateTime);
        }

        return timestamp;
    }

    private List<NotebookInfo> makeNotebookInfoList(List<Notebook> notebookList, Sort sort) {

        List<Homework> homeworkList = homeworkRepository.findAllByNotebooks(notebookList, sort);

        List<NotebookInfo> result = new ArrayList<>();

        int index = 0;
        for (Notebook notebook : notebookList) {
            List<HomeworkDto> homeworkDtoList = new ArrayList<>();
            boolean isDone = true;
            Homework homework;
            for (; index < homeworkList.size(); index++) {
                homework = homeworkList.get(index);
                if (homework.getNotebook() != notebook) {
                    break;
                }
                if (isDone & !homework.getIsDone()) {
                    isDone = false;
                }
                homeworkDtoList.add(HomeworkConverter.toDto(homework));
            }

            NotebookInfo notebookInfo = NotebookInfo.builder()
                    .notebookId(notebook.getId())
                    .studentName(notebook.getSchedule().getStudentName())
                    .subject(notebook.getSchedule().getSubject())
                    .deadline(notebook.getDeadline())
                    .isDone(isDone)
                    .homeworkDtoList(homeworkDtoList)
                    .build();

            result.add(notebookInfo);
        }

        return result;
    }
}
