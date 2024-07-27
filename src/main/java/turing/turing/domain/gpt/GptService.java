package turing.turing.domain.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import turing.turing.domain.gpt.dto.request.GPTRequestDto;
import turing.turing.domain.gpt.dto.response.GPTResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public GPTResponseDto getGptResponse(String prompt) {
        GPTRequestDto request = new GPTRequestDto(model, prompt, 1, 275, 1, 2, 2);
        return restTemplate.postForObject(apiUrl, request, GPTResponseDto.class);
    }

    public String parseData(GPTResponseDto gptResponse, String sectionTitle) {
        String content = "";

        for (GPTResponseDto.Choice choice : gptResponse.getChoices()) {
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
