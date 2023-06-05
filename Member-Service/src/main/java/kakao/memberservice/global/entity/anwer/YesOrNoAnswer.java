package kakao.memberservice.global.entity.anwer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kakao.memberservice.global.entity.MemberSurvey;
import kakao.memberservice.global.entity.question.YesOrNoQuestion;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "yesorno_answer")
public class YesOrNoAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private Boolean answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private YesOrNoQuestion yesOrNoQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_survey_id")
    private MemberSurvey memberSurvey;

    public void setAnswer(Boolean answer) {
    this.answer= answer;
    }
}