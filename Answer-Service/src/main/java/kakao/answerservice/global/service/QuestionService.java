package kakao.answerservice.global.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.answerservice.global.dto.CreateQuestionDto;
import kakao.answerservice.global.dto.CreateQuestionInput;
import kakao.answerservice.global.dto.PatchQuestoinDTO;
import kakao.answerservice.global.dto.SavedQuestionInput;
import kakao.answerservice.global.entity.Member;
import kakao.answerservice.global.entity.Survey;
import kakao.answerservice.global.entity.question.MultipleChoiceQuestion;
import kakao.answerservice.global.entity.question.SubjectiveQuestion;
import kakao.answerservice.global.entity.question.YesOrNoQuestion;
import kakao.answerservice.global.repository.MemberSurveyRepository;
import kakao.answerservice.global.repository.SurveyRepository;
import kakao.answerservice.global.repository.question.MultipleChoiceQuestionRepository;
import kakao.answerservice.global.repository.question.SubjectiveQuestionRepository;
import kakao.answerservice.global.repository.question.YesOrNoQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    private final YesOrNoQuestionRepository yesOrNoQuestionRepository;
    private final SubjectiveQuestionRepository subjectiveQuestionRepository;
    private final SurveyRepository surveyRepository;

    private final MemberSurveyRepository memberSurveyRepository;
    private final ObjectMapper mapper;



    public Survey createSurvey(CreateQuestionDto obj, Member member) {
        Survey survey = new Survey();
        survey.setId(obj.getSurveyId());
        survey.setIsOpen(obj.getVisibility());
        survey.setIsBrainwave(obj.getWearable());
        survey.setTitle(obj.getTitle());
        survey.setMember(member);
        survey.setStartDate(obj.getStartDate());
        survey.setEndDate(obj.getEndDate());

        return surveyRepository.save(survey);
    }

    public Survey createSurvey(PatchQuestoinDTO obj, Member member, Long survey_id) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(survey_id);
        if (!optionalSurvey.isPresent()) {
            throw new UsernameNotFoundException("Survey not found");
        }

        Survey survey = optionalSurvey.get();
        survey.setIsOpen(obj.getVisibility());
        survey.setIsBrainwave(obj.getWearable());
        survey.setTitle(obj.getTitle());
        survey.setStartDate(obj.getStartDate());
        survey.setEndDate(obj.getEndDate());

        survey.setMember(member);

        return surveyRepository.save(survey);
    }
    public Survey update(PatchQuestoinDTO dto, Authentication authentication,Long survey_id) {
        // Authentication 객체에서 사용자 이름 가져오기
        Member member = (Member) authentication.getPrincipal();

        // dto의 Survey Id로 Survey 정보 조회
        Survey surveyToUpdate = surveyRepository.findById(survey_id)
                .orElseThrow(() -> new UsernameNotFoundException("Survey could not be found."));

        // DTO로부터 변경할 정보를 가져와 Survey 정보 업데이트
        surveyToUpdate.setTitle(dto.getTitle());
        surveyToUpdate.setIsOpen(dto.getVisibility());
        surveyToUpdate.setIsBrainwave(dto.getWearable());
        surveyToUpdate.setMember(member);

        // 질문 업데이트
        patchQuestion(survey_id, dto.getSavedquestionList());

        // 변경된 설문 정보 저장
        return surveyRepository.save(surveyToUpdate);
    }


    public Void delete(Long survey_id, List<SavedQuestionInput> questionList) {
        // Survey Id로 Survey 정보 조회
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(() -> new UsernameNotFoundException("Survey could not be found."));

        // 설문조사에 연결된 질문 삭제
        deleteQuestion(survey_id, questionList);

        // Remaining questions from each type


        return null;
    }




    public void deleteQuestion(Long surveyId, List<SavedQuestionInput> questionList) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);

        if (!optionalSurvey.isPresent()) {
            throw new IllegalArgumentException("Survey not found");
        }
        Survey survey = optionalSurvey.get();

        for (SavedQuestionInput input : questionList) {
            switch (input.getType().toLowerCase()) {
                case "multiplechoice":
                    MultipleChoiceQuestion mcq = multipleChoiceQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));

                    // Ensure the question belongs to the correct survey before deleting
                    if (!mcq.getSurvey().equals(survey)) {
                        throw new IllegalArgumentException("Question does not belong to the specified survey");
                    }
                    multipleChoiceQuestionRepository.delete(mcq);
                    break;

                case "shortanswer":
                    SubjectiveQuestion sq = subjectiveQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));

                    // Ensure the question belongs to the correct survey before deleting
                    if (!sq.getSurvey().equals(survey)) {
                        throw new IllegalArgumentException("Question does not belong to the specified survey");
                    }
                    subjectiveQuestionRepository.delete(sq);
                    break;

                case "yesorno":
                    YesOrNoQuestion ynq = yesOrNoQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));

                    // Ensure the question belongs to the correct survey before deleting
                    if (!ynq.getSurvey().equals(survey)) {
                        throw new IllegalArgumentException("Question does not belong to the specified survey");
                    }
                    yesOrNoQuestionRepository.delete(ynq);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid question type");
            }
        }
    }

    public void patchQuestion(Long surveyId, List<SavedQuestionInput> questionList) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        int length = questionList.toArray().length;
        if (!optionalSurvey.isPresent()) {
            throw new IllegalArgumentException("Survey not found");
        }
        Survey survey = optionalSurvey.get();




        for (SavedQuestionInput input : questionList) {
            switch (input.getType().toLowerCase()) {
                case "multiplechoice":
                    MultipleChoiceQuestion mcq = multipleChoiceQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
                    mcq.setSurvey(survey);
                   // mcq.setNum((input.getId().intValue()));
                    mcq.setTitle(input.getTitle());

                    List<Object> options = input.getOptions();
                    String[] texts = new String[5];

                    for (int j = 0; j < options.size() && j < 5; j++) {
                        texts[j] = ((Map<String, String>) ((List<?>) options).get(j)).get("text");
                       ;
                    }
                    mcq.setChoice(texts[0], texts[1], texts[2], texts[3], texts[4]);
                    mcq.setCount(options.size());
                    break;

                case "shortanswer":
                    SubjectiveQuestion sq = subjectiveQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
                    sq.setSurvey(survey);
                    // sq.setNum((input.getId().intValue()));
                    sq.setQuestion(input.getTitle());
                    break;

                case "yesorno":
                    YesOrNoQuestion ynq = yesOrNoQuestionRepository.findById(input.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
                    ynq.setSurvey(survey);
                    //ynq.setNum((input.getId().intValue()));
                    ynq.setQuestion(input.getTitle());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid question type");
            }
        }
    }



    public void createQuestion(Survey survey, List<CreateQuestionInput> questionList) {
        int length = questionList.toArray().length;

        for (int i = 0; i < length; i++) {
            if (questionList.get(i).getType().equalsIgnoreCase("multipleChoice")) {
                MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
                multipleChoiceQuestion.setSurvey(survey);
                multipleChoiceQuestion.setNum(questionList.get(i).getId());
                multipleChoiceQuestion.setTitle(questionList.get(i).getTitle());
                List<Object> options = questionList.get(i).getOptions();
                String[] texts = new String[5];

                for (int j = 0; j < options.size() && j < 5; j++) {
                    texts[j] = ((Map<String, String>) ((List<?>) options).get(j)).get("text");
                }
                multipleChoiceQuestion.setChoice(texts[0], texts[1] , texts[2] , texts[3] , texts[4]);
                multipleChoiceQuestion.setCount(options.size());
                multipleChoiceQuestionRepository.save(multipleChoiceQuestion);

            } else if (questionList.get(i).getType().equalsIgnoreCase("shortAnswer")) {
                SubjectiveQuestion subjectiveQuestion = new SubjectiveQuestion();
                subjectiveQuestion.setSurvey(survey);
                subjectiveQuestion.setNum(questionList.get(i).getId());
                subjectiveQuestion.setQuestion(questionList.get(i).getTitle());
                subjectiveQuestionRepository.save(subjectiveQuestion);

            } else if (questionList.get(i).getType().equalsIgnoreCase("yesOrNo")) {
                YesOrNoQuestion yesOrNoQuestion = new YesOrNoQuestion();
                yesOrNoQuestion.setSurvey(survey);
                yesOrNoQuestion.setNum(questionList.get(i).getId());
                yesOrNoQuestion.setQuestion(questionList.get(i).getTitle());

                yesOrNoQuestionRepository.save(yesOrNoQuestion);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public Survey findQuestionById(Long id) {
        //surveyRepository.findById(survey_id);
        Optional<Survey> findSurvey = surveyRepository.findById(id);
        if (findSurvey.isPresent()) {
            return findSurvey.get();
        }
        return null;
}

    public List<Survey> findAllSurveyIMade(Member member) {
        List<Survey> allSurveyByMember = surveyRepository.findAllByMember(member);
        return allSurveyByMember;
    }

    public List<Survey> findAllSurveyIAnswered(Member member) {
        List<Survey> memberSurveyByMemberId = memberSurveyRepository.findMemberSurveyByMemberId(member.getId());
        log.info("응답 내용 개수={}",memberSurveyByMemberId.size());
        return memberSurveyByMemberId;
    }

    public Survey getSurveyStatistic(Long surveyId) {
        Optional<Survey> survey = surveyRepository.findById(surveyId);
        if (!survey.isPresent()) {
            // 에러 처리
        }
                return survey.get();

//        Survey survey = surveyRepository.findSurveyById(surveyId);
//        return survey;

    }
//    public MultipleChoiceQuestion getSurveyStatisticTest(Long surveyId) {
//
//        Optional<YesOrNoQuestion> bySurvey = yesOrNoQuestionRepository.findBySurveyId(surveyId);
//        Optional<MultipleChoiceQuestion> bySurveyId = multipleChoiceQuestionRepository.findBySurveyId(surveyId);
//        return bySurveyId.get();
//
//    }
}

