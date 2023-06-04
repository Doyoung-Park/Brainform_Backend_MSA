package kakao.answerservice.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import kakao.answerservice.global.dto.AnswerDTO;
import kakao.answerservice.global.dto.FilterDTO;
import kakao.answerservice.global.entity.Member;
import kakao.answerservice.global.entity.MemberSurvey;
import kakao.answerservice.global.entity.Survey;
import kakao.answerservice.global.repository.MemberRepository;
import kakao.answerservice.global.repository.MemberSurveyRepository;
import kakao.answerservice.global.repository.SurveyRepository;
import kakao.answerservice.global.repository.question.MultipleChoiceQuestionRepository;
import kakao.answerservice.global.repository.question.SubjectiveQuestionRepository;
import kakao.answerservice.global.repository.question.YesOrNoQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSurveyService {

    private final SurveyRepository surveyRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final QuestionService questionService;
    private final YesOrNoQuestionRepository yesOrNoQuestionRepository;
    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    private final SubjectiveQuestionRepository subjectiveQuestionRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper mapper;
    private final EntityManager em;
    public MemberSurvey createMemberSurvey(Member member, Long surveyId) {
        Survey survey = surveyRepository.findSurveyById(surveyId).get();  //
        MemberSurvey memberSurvey = new MemberSurvey();
        memberSurvey.setMember(member);
        memberSurvey.setSurvey(survey);

        return memberSurveyRepository.save(memberSurvey);
    }

    @Transactional
    public Survey getDataWithFilter(FilterDTO filterDTO) throws JsonProcessingException {

        log.info("필터링 진입");
        Survey surveyById = surveyRepository.findSurveyById(filterDTO.getSurveyId()).get();

        log.info("필터링 진행");
        List<MemberSurvey> memberSurveyFilter = memberSurveyRepository.getMemberSurveyFilter(filterDTO);
        log.info("필터링 길이={}",memberSurveyFilter.get(0).getMember().getUsername());

        AnswerDTO answerDTO = new AnswerDTO();
        for (MemberSurvey memberSurvey : memberSurveyFilter) {

            log.info("응답 내용={}",memberSurvey.getMultipleChoiceAnswers().size());
            answerDTO.updateAnswer(memberSurvey);
        }

        String answerDTOtoString = mapper.writeValueAsString(answerDTO);
        log.info("응답 내용 통합={}", answerDTOtoString);

        log.info("필터링 완료");
        Survey survey = surveyById.filterQuestions(answerDTO);


        log.info("필터링 된 dto={}", mapper.writeValueAsString(survey));

        return survey;
    }
}
