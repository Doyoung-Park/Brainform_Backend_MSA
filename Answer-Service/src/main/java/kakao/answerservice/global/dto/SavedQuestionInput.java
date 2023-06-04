package kakao.answerservice.global.dto;

import lombok.Data;

import java.util.List;
@Data
public class SavedQuestionInput {
    private Long id;

    private int num;
    private String title;


    private String type;
    private List<Object> options;


}
