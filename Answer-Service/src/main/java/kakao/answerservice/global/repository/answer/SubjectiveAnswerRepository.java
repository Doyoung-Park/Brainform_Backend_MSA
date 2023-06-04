package kakao.answerservice.global.repository.answer;

import kakao.answerservice.global.entity.anwer.SubjectiveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectiveAnswerRepository extends JpaRepository<SubjectiveAnswer, Long> {
}
