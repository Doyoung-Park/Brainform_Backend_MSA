package kakao.readservice.global.entity.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kakao.readservice.global.entity.Survey;
import kakao.readservice.global.entity.anwer.SubjectiveAnswer;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subjective_question")
public class SubjectiveQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private Integer num;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "subjectiveQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubjectiveAnswer> subjectiveAnswers;

    public void setQuestion(String title) {
        this.title = title;
    }

    public SubjectiveQuestion filterAnswer(List<SubjectiveAnswer> answers) {
        SubjectiveQuestion subjectiveQuestion = SubjectiveQuestion.builder()
                .id(this.id)
                .num(this.num)
                .title(this.title)
                .subjectiveAnswers(answers)
                .build();

        return subjectiveQuestion;
    }
}
