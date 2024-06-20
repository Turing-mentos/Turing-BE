package turing.turing.domain.gpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import turing.turing.domain.gpt.dto.GPTRequest;
import turing.turing.domain.gpt.dto.GPTResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public GPTResponse getGptResponse(String prompt) {
        GPTRequest request = new GPTRequest(model, prompt, 1, 275, 1, 2, 2);
        return restTemplate.postForObject(apiUrl, request, GPTResponse.class);
    }

    public String parseData(GPTResponse gptResponse, String sectionTitle) {
        String content = "";

        for (GPTResponse.Choice choice : gptResponse.getChoices()) {
            String messageContent = choice.getMessage().getContent();

            if (messageContent.contains(sectionTitle)) {
                int startIndex = messageContent.indexOf(sectionTitle) + sectionTitle.length();
                //  ']' 확인
                if (messageContent.charAt(startIndex) == ']') {
                    startIndex++;
                }
                content = messageContent.substring(startIndex).trim();

                // section title있으면 , truncate the content
                if (content.contains("[")) {
                    content = content.substring(0, content.indexOf("[")).trim();
                }

                break;
            }
        }

        return content;
    }
}
