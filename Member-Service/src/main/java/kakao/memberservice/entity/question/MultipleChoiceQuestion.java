package kakao.memberservice.entity.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kakao.memberservice.entity.Survey;
import kakao.memberservice.entity.anwer.MultipleChoiceAnswer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "multiplechoice_question")
@Slf4j
public class MultipleChoiceQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;


    private Integer num;
    private String title;

    private Integer count;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "multipleChoiceQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultipleChoiceAnswer> multipleChoiceAnswers;

    public void setChoice(String o, String o1, String o2, String o3, String o4) {
        this.choice1 = o;
        this.choice2 = o1;
        this.choice3 = o2;
        this.choice4 = o3;
        this.choice5 = o4;
    }

    public MultipleChoiceQuestion filterAnswer(List<MultipleChoiceAnswer> answers) {

        List<MultipleChoiceAnswer> tmp = new ArrayList<>();
        for (MultipleChoiceAnswer multipleChoiceAnswer : answers) {
            if (multipleChoiceAnswer.getMultipleChoiceQuestion().getId().equals(this.id)) {
                tmp.add(multipleChoiceAnswer);
            }
        }
        MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestion.builder()
                .id(this.id)
                .num(this.num)
                .title(this.title)
                .count(this.count)
                .choice1(this.choice1)
                .choice2(this.choice2)
                .choice3(this.choice3)
                .choice4(this.choice4)
                .choice5(this.choice5)
                .survey(this.survey)
                .multipleChoiceAnswers(tmp).build();
        return multipleChoiceQuestion;
    }
}
