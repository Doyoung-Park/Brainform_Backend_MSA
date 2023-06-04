package kakao.memberservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kakao.memberservice.dto.AnswerDTO;
import kakao.memberservice.entity.question.MultipleChoiceQuestion;
import kakao.memberservice.entity.question.SubjectiveQuestion;
import kakao.memberservice.entity.question.YesOrNoQuestion;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    private String title;

    @Column(name = "is_open")
    private String isOpen;

    @Column(name = "is_brainwave")
    private String isBrainwave;

    @Column(name = "answer_period")
    private Date answerPeriod;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surveyor_id")
    private Member member;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @JsonIgnore
    private List<MemberSurvey> memberSurveys;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<YesOrNoQuestion> yesOrNoQuestions;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubjectiveQuestion> subjectiveQuestions;

    public Survey filterQuestions(AnswerDTO answerDTO) {
            this.multipleChoiceQuestions = this.multipleChoiceQuestions.stream().map(
                    question -> {
                        MultipleChoiceQuestion filteredQuestion = question.filterAnswer(answerDTO.getMultipleChoiceAnswers());
                        return filteredQuestion;
                    }).collect(Collectors.toList());
            this.yesOrNoQuestions = this.yesOrNoQuestions.stream().map(
                    question -> {
                        YesOrNoQuestion yesOrNoQuestion = question.filterAnswer(answerDTO.getYesOrNoAnswers());
                        return yesOrNoQuestion;
                    }).collect(Collectors.toList());

            this.subjectiveQuestions = this.subjectiveQuestions.stream().map(
                    question -> {
                        SubjectiveQuestion subjectiveQuestion = question.filterAnswer(answerDTO.getSubjectiveAnswers());
                        return subjectiveQuestion;
                    }).collect(Collectors.toList());

        return this;
    }

}
