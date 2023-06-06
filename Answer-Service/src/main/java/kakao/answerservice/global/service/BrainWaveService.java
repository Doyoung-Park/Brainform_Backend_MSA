package kakao.answerservice.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.answerservice.global.dto.BrainDataDTO;
import kakao.answerservice.global.entity.BrainMemberInfo;
import kakao.answerservice.global.entity.BrainwaveResult;
import kakao.answerservice.global.entity.MemberSurvey;
import kakao.answerservice.global.repository.BrainWaveCodeRepository;
import kakao.answerservice.global.repository.BrainWaveRepository;
import kakao.answerservice.global.repository.MemberSurveyRepository;
import kakao.answerservice.global.repository.SurveyRepository;
import kakao.answerservice.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrainWaveService {

    private final BrainWaveCodeRepository codeRepository;
    private final BrainWaveRepository brainWaveRepository;
    private final SurveyRepository surveyRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final FileUtil fileUtil;

    private final ObjectMapper mapper;

    @Transactional
    public ResponseEntity<?> saveBrainWave(BrainDataDTO dto) throws JsonProcessingException {

        Optional<BrainMemberInfo> byCode = codeRepository.findByCode(dto.getCode());
        if (byCode.isEmpty()) {
            return new ResponseEntity<>("뇌파 측정 코드가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        BrainMemberInfo brainMemberInfo = byCode.get();


        MemberSurvey memberSurvey = memberSurveyRepository
                .findMemberSurveyBySurveyIdAndMemberID(brainMemberInfo.getSurveyId(), brainMemberInfo.getMemberId());

        BrainwaveResult brainwaveResult = BrainwaveResult.builder()
                .attAvg(dto.getAvgAtt())
                .meditAvg(dto.getAvgMed())
                .img(fileUtil.saveUserImage(dto.getImage()))
                .build();

        brainWaveRepository.save(brainwaveResult);
        MemberSurvey survey = memberSurvey.setBrainWaveData(brainwaveResult);
        String s = mapper.writeValueAsString(survey);
        log.info("member-survey={}", s);
        memberSurveyRepository.save(survey);
        return new ResponseEntity<>("뇌파 데이터 저장 완료", HttpStatus.OK);
    }
}
