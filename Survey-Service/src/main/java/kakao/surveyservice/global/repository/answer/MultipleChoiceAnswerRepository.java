package kakao.surveyservice.global.repository.answer;


import kakao.surveyservice.global.entity.anwer.MultipleChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceAnswerRepository extends JpaRepository<MultipleChoiceAnswer, Long> {
}
