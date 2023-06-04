package kakao.surveyservice.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceAnsDto {

    private int questionNum;
    private String answer;

}
