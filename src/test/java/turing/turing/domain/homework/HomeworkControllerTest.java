package turing.turing.domain.homework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import turing.turing.domain.ControllerTestSupport;
import turing.turing.domain.homework.dto.CreateHomeworkRequest;

public class HomeworkControllerTest extends ControllerTestSupport {

    @DisplayName("숙제를 생성한다.")
    @Test
    void createHomework() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/homework/1"));
    }

    @DisplayName("숙제를 생성할 때 카테고리는 필수입니다.")
    @Test
    void createHomeworkWithoutCategory() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("카테고리는 필수입니다."));
    }

    @DisplayName("숙제를 생성할 때 제목은 필수입니다.")
    @Test
    void createHomeworkWithoutTitle() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("제목은 필수입니다."));
    }

    @DisplayName("숙제를 생성할 때 범위타입은 필수입니다.")
    @Test
    void createHomeworkWithoutRangeType() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .title("제목")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("범위타입은 필수입니다."));
    }

    @DisplayName("숙제를 생성할 때 끝범위는 시작범위보다 크거나 같아야 합니다.")
    @Test
    void createHomeworkWithRangeStartBiggerThanEnd() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(10)
                .rangeEnd(1)
                .content("내용")
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("끝범위는 시작범위와 같거나 커야 합니다."));
    }

    @DisplayName("숙제를 생성할 때 내용은 필수입니다.")
    @Test
    void createHomeworkWithoutContent() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .notebookId(1L)
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("내용은 필수입니다."));
    }

    @DisplayName("숙제를 생성할 때 알림장Id는 필수입니다.")
    @Test
    void createHomeworkWithoutNotebookId() throws Exception {
        // given
        CreateHomeworkRequest request = CreateHomeworkRequest.builder()
                .category("카테고리")
                .title("제목")
                .rangeType("범위타입")
                .rangeStart(1)
                .rangeEnd(10)
                .content("내용")
                .build();

        when(homeworkService.createHomework(any(CreateHomeworkRequest.class)))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/homework")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("알림장Id는 필수입니다."));
    }

    @DisplayName("숙제를 삭제한다.")
    @Test
    void deleteHomework() throws Exception {
        // given // when // then
        Long homeworkId = 1L;
        mockMvc.perform(
                delete("/api/homework/{id}", homeworkId)
        ).andExpect(status().isNoContent());
    }

}
