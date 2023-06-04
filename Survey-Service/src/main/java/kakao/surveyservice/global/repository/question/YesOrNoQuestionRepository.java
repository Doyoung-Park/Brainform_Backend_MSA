package kakao.surveyservice.global.repository.question;

import kakao.surveyservice.global.entity.question.YesOrNoQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YesOrNoQuestionRepository extends JpaRepository<YesOrNoQuestion, Long> {


    YesOrNoQuestion save(YesOrNoQuestion yesOrNoQuestion);

    Optional<YesOrNoQuestion> findBySurveyId(Long surveyId);

    YesOrNoQuestion findYesOrNoQuestionById(Long id);

    YesOrNoQuestion findAllBySurveyId(Long surveyId);
}
