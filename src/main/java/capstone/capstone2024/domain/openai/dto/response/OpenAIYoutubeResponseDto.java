package capstone.capstone2024.domain.openai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIYoutubeResponseDto {
    private List<Choice> choices;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private Message message;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            private Content content;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Content {
                private String title;
                private String category;
            }
        }
    }
}
