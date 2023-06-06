package kakao.answerservice.global.entity;

import jakarta.persistence.*;
import kakao.answerservice.global.entity.anwer.MultipleChoiceAnswer;
import kakao.answerservice.global.entity.anwer.SubjectiveAnswer;
import kakao.answerservice.global.entity.anwer.YesOrNoAnswer;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_survey")
public class MemberSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_survey_id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private BrainwaveResult brainwaveResult;

    @JsonIgnore
    @OneToMany(mappedBy = "memberSurvey", fetch = FetchType.LAZY)
    private List<MultipleChoiceAnswer> multipleChoiceAnswers;

    @JsonIgnore
    @OneToMany(mappedBy = "memberSurvey", fetch = FetchType.LAZY)
    private List<SubjectiveAnswer> subjectiveAnswers;

    @JsonIgnore
    @OneToMany(mappedBy = "memberSurvey", fetch = FetchType.LAZY)
    private List<YesOrNoAnswer> yesOrNoAnswers;

    public MemberSurvey setBrainWaveData(BrainwaveResult result) {
        this.brainwaveResult = result;
        return this;
    }
}
