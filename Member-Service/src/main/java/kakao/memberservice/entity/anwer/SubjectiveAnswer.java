package kakao.memberservice.entity.anwer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kakao.memberservice.entity.MemberSurvey;
import kakao.memberservice.entity.question.SubjectiveQuestion;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subjective_answer")
public class SubjectiveAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private SubjectiveQuestion subjectiveQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_survey_id")
    private MemberSurvey memberSurvey;

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
