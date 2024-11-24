package turing.turing.domain.exam;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import turing.turing.domain.ControllerTestSupport;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.CreateExamResponse;
import turing.turing.domain.exam.dto.UpdateExamRequest;

class ExamControllerTest extends ControllerTestSupport {

    @DisplayName("시험일정을 등록한다.")
    @Test
    void createExamSchedule() throws Exception {
        // given
        CreateExamRequest request = CreateExamRequest.builder()
                .examName("시험명")
                .startDate(LocalDate.of(2024,11,16))
                .endDate(LocalDate.of(2024,11,17))
                .studyRoomId(1L)
                .build();

        CreateExamResponse response = CreateExamResponse.builder()
                .examId(1L)
                .build();

        when(examService.createExamSchedules(any(), any(CreateExamRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(
                        post("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/exam/1"));
    }

    @DisplayName("시험을 등록할 때 시험명은 필수값이다.")
    @Test
    void createExamScheduleWithoutExamName() throws Exception {
        // given
        CreateExamRequest request = CreateExamRequest.builder()
                .examName("")
                .startDate(LocalDate.of(2024,11,16))
                .endDate(LocalDate.of(2024,11,17))
                .studyRoomId(1L)
                .build();

        CreateExamResponse response = CreateExamResponse.builder()
                .examId(1L)
                .build();

        when(examService.createExamSchedules(any(), any(CreateExamRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(
                        post("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("시험명은 필수입니다."));
    }

    @DisplayName("시험을 등록할 때 종료일은 시작일보다 과거여선 안 된다.")
    @Test
    void createExamScheduleWithEndDateBeforeStartDate() throws Exception {
        // given
        CreateExamRequest request = CreateExamRequest.builder()
                .examName("시험명")
                .startDate(LocalDate.of(2024,11,17))
                .endDate(LocalDate.of(2024,11,16))
                .studyRoomId(1L)
                .build();

        CreateExamResponse response = CreateExamResponse.builder()
                .examId(1L)
                .build();

        when(examService.createExamSchedules(any(), any(CreateExamRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(
                        post("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("종료일은 시작일과 같거나 미래여야 합니다."));
    }

    @DisplayName("시험을 등록할 때 과외공간Id는 필수값이다.")
    @Test
    void test() throws Exception {
        // given
        CreateExamRequest request = CreateExamRequest.builder()
                .examName("시험명")
                .startDate(LocalDate.of(2024,11,16))
                .endDate(LocalDate.of(2024,11,17))
                .build();

        CreateExamResponse response = CreateExamResponse.builder()
                .examId(1L)
                .build();

        when(examService.createExamSchedules(any(), any(CreateExamRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(
                        post("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("과외공간Id는 필수입니다."));
    }

    @DisplayName("시험일정을 수정한다.")
    @Test
    void modifyExamSchedule() throws Exception {
        // given
        UpdateExamRequest request = UpdateExamRequest.builder()
                .examId(1L)
                .examName("시험명")
                .startDate(LocalDate.of(2024, 11, 16))
                .endDate(LocalDate.of(2024, 11, 17))
                .studyRoomId(1L)
                .build();

        when(examService.updateExamSchedule(any(UpdateExamRequest.class)))
                .thenReturn(request.getExamId());
        // when then
        mockMvc.perform(
                        patch("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(request.getExamId()));
    }

    @DisplayName("시험일정을 수정할 때 시험Id는 필수값이다.")
    @Test
    void modifyExamScheduleWithoutExamId() throws Exception {
        // given
        UpdateExamRequest request = UpdateExamRequest.builder()
                .examName("시험명")
                .startDate(LocalDate.of(2024, 11, 16))
                .endDate(LocalDate.of(2024, 11, 17))
                .studyRoomId(1L)
                .build();

        when(examService.updateExamSchedule(any(UpdateExamRequest.class)))
                .thenReturn(request.getExamId());
        // when then
        mockMvc.perform(
                        patch("/exam")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("시험Id는 필수입니다."));
    }
}