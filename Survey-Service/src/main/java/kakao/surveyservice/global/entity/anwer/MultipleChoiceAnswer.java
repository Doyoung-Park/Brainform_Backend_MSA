package kakao.surveyservice.global.entity.anwer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kakao.surveyservice.global.entity.MemberSurvey;
import kakao.surveyservice.global.entity.question.MultipleChoiceQuestion;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "multiplechoice_answer")
public class MultipleChoiceAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_survey_id")
    private MemberSurvey memberSurvey;

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}



