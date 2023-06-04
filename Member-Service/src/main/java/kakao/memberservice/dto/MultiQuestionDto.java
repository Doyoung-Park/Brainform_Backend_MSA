package kakao.memberservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultiQuestionDto implements Serializable {


    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String type;

    @NotNull
    private List<?> options;

    @NotNull
    private String choice1;
    @NotNull
    private String choice2;
    @NotNull
    private String choice3;
    @NotNull
    private String choice4;
    @NotNull
    private String choice5;


}



