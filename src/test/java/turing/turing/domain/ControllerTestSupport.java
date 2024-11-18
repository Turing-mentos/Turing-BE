package turing.turing.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.exam.ExamController;
import turing.turing.domain.exam.ExamService;
import turing.turing.global.security.JwtAuthenticationFilter;

@WebMvcTest(controllers = {
        ExamController.class
})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ExamService examService;

    @MockBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    protected CustomUserDetails customUserDetails;
}
