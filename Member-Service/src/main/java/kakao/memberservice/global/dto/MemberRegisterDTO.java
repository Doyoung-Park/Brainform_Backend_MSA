package kakao.memberservice.global.dto;

import jakarta.validation.constraints.NotEmpty;
import kakao.memberservice.global.entity.Member;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberRegisterDTO implements Serializable {

    @NotEmpty
    private String nickname;
    @NotEmpty
    private String gender;
    @NotEmpty
    private String age;
    @NotEmpty
    private String job;

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .job(job)
                .build();
    }
}
