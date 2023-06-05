package kakao.memberservice.global.repository.answer;

import kakao.memberservice.global.entity.anwer.SubjectiveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectiveAnswerRepository extends JpaRepository<SubjectiveAnswer, Long> {
}
