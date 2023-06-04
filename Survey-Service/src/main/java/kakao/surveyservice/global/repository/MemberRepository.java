package kakao.surveyservice.global.repository;

import kakao.surveyservice.global.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByJob(String jobs);
}
