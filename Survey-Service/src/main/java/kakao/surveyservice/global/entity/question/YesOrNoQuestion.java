package kakao.surveyservice.global.entity.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kakao.surveyservice.global.entity.Survey;
import kakao.surveyservice.global.entity.anwer.YesOrNoAnswer;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "yesorno_question")
public class YesOrNoQuestion {
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

    @OneToMany(mappedBy = "yesOrNoQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<YesOrNoAnswer> yesOrNoAnswer;

    public void setQuestion(String title) {
        this.title = title;
    }

    public YesOrNoQuestion filterAnswer(List<YesOrNoAnswer> answers) {
        YesOrNoQuestion yesOrNoQuestion = YesOrNoQuestion.builder()
                .id(this.id)
                .num(this.num)
                .title(this.title)
                .yesOrNoAnswer(answers)
                .build();

        return yesOrNoQuestion;
    }
}
