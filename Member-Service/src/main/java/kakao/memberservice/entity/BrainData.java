package kakao.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrainData {
    private String memberId;
    private String surveyId;
    private String code;
    private byte[] image;

    private double avgAtt;

    private double avgMed;


}
