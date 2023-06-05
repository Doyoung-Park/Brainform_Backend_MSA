package kakao.readservice.global.repository.question;

import kakao.readservice.global.entity.question.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultipleChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {


    MultipleChoiceQuestion save(MultipleChoiceQuestion multipleChoiceQuestion);
    Optional<MultipleChoiceQuestion> findBySurveyId(Long id);

    MultipleChoiceQuestion findMultipleChoiceQuestionById(Long id);
    //    Iterable<YesOrNoQuestion> saveAll(Iterable<YesOrNoQuestion> yesOrNoQuestions);


//    void save(QuestionInterface questionInterface);
}
