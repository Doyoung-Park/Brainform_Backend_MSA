package kakao.readservice.global.repository;

import kakao.readservice.global.entity.BrainMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrainWaveCodeRepository {

    private final Map<String, BrainMemberInfo> map;

    public BrainMemberInfo save(BrainMemberInfo brainMemberInfo) {

        map.put(brainMemberInfo.getCode(), brainMemberInfo);

        return brainMemberInfo;
    }

    public Optional<BrainMemberInfo> findByCode(String code) {
        return Optional.ofNullable(map.get(code));
    }
}
