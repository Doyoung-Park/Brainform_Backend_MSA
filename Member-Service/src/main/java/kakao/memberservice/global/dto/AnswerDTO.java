package kakao.memberservice.global.dto;

import kakao.memberservice.global.entity.MemberSurvey;
import kakao.memberservice.global.entity.anwer.MultipleChoiceAnswer;
import kakao.memberservice.global.entity.anwer.SubjectiveAnswer;
import kakao.memberservice.global.entity.anwer.YesOrNoAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private List<MultipleChoiceAnswer> multipleChoiceAnswers = new ArrayList<>();
    private List<SubjectiveAnswer> subjectiveAnswers = new ArrayList<>();
    private List<YesOrNoAnswer> yesOrNoAnswers = new ArrayList<>();

    public void updateAnswer(MemberSurvey memberSurvey) {
        this.multipleChoiceAnswers.addAll(memberSurvey.getMultipleChoiceAnswers());
        this.subjectiveAnswers.addAll(memberSurvey.getSubjectiveAnswers());
        this.yesOrNoAnswers.addAll(memberSurvey.getYesOrNoAnswers());
    }


}
