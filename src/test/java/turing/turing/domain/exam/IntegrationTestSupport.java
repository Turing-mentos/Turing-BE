package turing.turing.domain.exam;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.notice.fcm.aop.AfterAspect;

@SpringBootTest
@Transactional
public abstract class IntegrationTestSupport {

    @MockBean
    protected AfterAspect afterAspect;

}
