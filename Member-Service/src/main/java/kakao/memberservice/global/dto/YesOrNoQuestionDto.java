package kakao.memberservice.global.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class YesOrNoQuestionDto {
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String type;

}
