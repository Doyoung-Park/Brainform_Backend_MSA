package kakao.memberservice.global.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnswerDto {

    @JsonProperty("surveyId")
    private Long surveyId;
    @JsonProperty("answers")
    private ArrayList<CreateAnswerInput> answers;

    //private String type;

}
