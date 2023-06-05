package kakao.memberservice.global.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionDto{

    @NotNull
    private String title;

    @NotNull
    private List<CreateQuestionInput> questionList;



    @NotNull
    private String visibility;

    @NotNull
    private String wearable;

    @NotNull
    private Long surveyId;
}
