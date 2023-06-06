package kakao.answerservice.global.dto;

import kakao.answerservice.global.entity.anwer.MultipleChoiceAnswer;
import kakao.answerservice.global.entity.anwer.SubjectiveAnswer;
import kakao.answerservice.global.entity.anwer.YesOrNoAnswer;
import kakao.answerservice.global.entity.question.MultipleChoiceQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long surveyeeId;
    private Long id;
    private Integer num;
//    private AnswerDTO answerDTOS = new AnswerDTO();
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;
    private List<MultipleChoiceAnswer> multipleChoiceAnswers;

    private List<SubjectiveAnswer> subjectiveAnswers;
    private List<YesOrNoAnswer> yesOrNoAnswers;
}
