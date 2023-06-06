package kakao.answerservice.global.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private String visibility;

    @NotNull
    private String wearable;

    @NotNull
    private Long surveyId;
}
