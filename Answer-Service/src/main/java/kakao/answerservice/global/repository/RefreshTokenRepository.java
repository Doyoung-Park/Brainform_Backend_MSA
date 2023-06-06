package kakao.answerservice.global.repository;

import kakao.answerservice.global.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken save(RefreshToken refreshToken);

}
