package kakao.readservice.global.repository;


import kakao.readservice.global.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken save(RefreshToken refreshToken);

}
