package kakao.memberservice.entity;


import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrainMemberInfo {

    private String code;
    private Long surveyId;
    private Long memberId;
    private boolean flag;
}
