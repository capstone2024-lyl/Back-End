package capstone.capstone2024.domain.openai.application;

import capstone.capstone2024.domain.openai.dto.request.OpenAIRequestDto;
import capstone.capstone2024.domain.openai.dto.response.OpenAIResponseDto;
import capstone.capstone2024.global.error.ErrorCode;
import capstone.capstone2024.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${spring.openai.api.key}")
    private String openaiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String translateText(String text) {
        WebClient client = WebClient.builder()
                .baseUrl(OPENAI_URL)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        OpenAIRequestDto request = OpenAIRequestDto.builder()
                .model("gpt-4o")
                .messages(List.of(Map.of("role", "user", "content", "please translate given text file(wrote in sns) korean to english without enter('\n). ex) the results we're looking forward to is '아 진짜 개웃기네 ㅋㅋㅋ' translated to 'oh it's so funny lmao': \n" + text)))
                .build();

        OpenAIResponseDto response = client.post()
                .body(Mono.just(request), OpenAIRequestDto.class)
                .retrieve()
                .bodyToMono(OpenAIResponseDto.class)
                .block();

        if (response == null || response.getChoices().isEmpty()) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from OpenAI");
        }

        return response.getChoices().get(0).getMessage().get("content").trim();
    }




    public String analyzeMBTI(String chat, String mbti) {
        WebClient client = WebClient.builder()
                .baseUrl(OPENAI_URL)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        OpenAIRequestDto request = OpenAIRequestDto.builder()
                .model("gpt-4o")
                .messages(List.of(Map.of("role", "user", "content", "We predicted that the MBTI of the given text file is mbti:\n" + mbti +"\n"+
                        "I'll give you the chat and tell me which sentence is the strongest basis for " + mbti + "\n" +
                        "find four reasons for all components\n" +
                        "And you must tell me the result only for Korean\n" +
                        "\n" +
                        "example : '나랑 만날래?'로 보아 E 성향이 나타나는 것을 알 수 있습니다. \n"+ chat)))
                .build();

        OpenAIResponseDto response = client.post()
                .body(Mono.just(request), OpenAIRequestDto.class)
                .retrieve()
                .bodyToMono(OpenAIResponseDto.class)
                .block();

        if (response == null || response.getChoices().isEmpty()) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from OpenAI");
        }

        return response.getChoices().get(0).getMessage().get("content").trim();
    }

    @Transactional
    public String youtubeSearch(String channelName) {

        WebClient client = WebClient.builder()
                .baseUrl(OPENAI_URL)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        OpenAIRequestDto request = OpenAIRequestDto.builder()
                .model("gpt-4o")
                .messages(List.of(Map.of(
                        "role", "user",
                        "content", "Find the YouTube channel category for the following channel names by searching on Namuwiki or YouTube. Provide the result in a JSON format with \"channelName\" and \"category\" fields for each channel. \n" +
                                "\n" +
                                "Channel Names:\n" +
                                "- 채널명1\n" +
                                "- 채널명2\n" +
                                "- 채널명3\n" +
                                "\n" +
                                "Example JSON response:\n" +
                                "[\n" +
                                "  {\"channelName\": \"채널명1\", \"category\": \"카테고리1\"},\n" +
                                "  {\"channelName\": \"채널명2\", \"category\": \"카테고리2\"},\n" +
                                "  {\"channelName\": \"채널명3\", \"category\": \"카테고리3\"}\n" +
                                "]\n" + channelName
                )))
                .build();

        OpenAIResponseDto response = client.post()
                .body(Mono.just(request), OpenAIRequestDto.class)
                .retrieve()
                .bodyToMono(OpenAIResponseDto.class)
                .block();

        if (response == null || response.getChoices().isEmpty()) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from OpenAI");
        }

        System.out.println("response = " + response);


        return response.getChoices().get(0).getMessage().get("content").trim();

//        List<OpenAIYoutubeResponseDto.Choice.Message.Content> contents = response.getChoices().stream()
//                .map(OpenAIYoutubeResponseDto.Choice::getMessage)
//                .map(OpenAIYoutubeResponseDto.Choice.Message::getContent)
//                .collect(Collectors.toList());

//        return contents.stream()
//                .map(content -> YoutubeSubscribeResponseDto.builder()
//                        .title(content.getTitle())
//                        .category(content.getCategory())
//                        .build())
//                .collect(Collectors.toList());

    }




}
