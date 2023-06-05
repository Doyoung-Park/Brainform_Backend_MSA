package kakao.readservice.global.dto;

import lombok.Data;

import java.util.List;
@Data
public class CreateQuestionInput {
    private Integer id;

    private String title;


    private String type;
    private List<Object> options;
}
