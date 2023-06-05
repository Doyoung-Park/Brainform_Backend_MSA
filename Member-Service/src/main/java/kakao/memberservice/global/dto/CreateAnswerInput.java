package kakao.memberservice.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnswerInput {

    private Long questionId;

    @JsonProperty("questionNum")
    private int questionNum;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("type")
    private String type;}
