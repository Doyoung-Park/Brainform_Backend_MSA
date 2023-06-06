package kakao.answerservice.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrainDataDTO {
    private Long memberId;
    private Long surveyId;
    private String code;
    private MultipartFile image;

    private Float avgAtt;

    private Float avgMed;

}
