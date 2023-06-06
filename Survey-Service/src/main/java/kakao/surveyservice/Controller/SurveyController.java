package kakao.surveyservice.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.surveyservice.global.dto.CreateQuestionDto;
import kakao.surveyservice.global.dto.CreateQuestionInput;
import kakao.surveyservice.global.dto.PatchQuestoinDTO;
import kakao.surveyservice.global.entity.Member;
import kakao.surveyservice.global.entity.Survey;
import kakao.surveyservice.global.service.AnswerService;
import kakao.surveyservice.global.service.MemberSurveyService;
import kakao.surveyservice.global.service.QuestionService;
import kakao.surveyservice.global.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {


    private final QuestionService questionService;

    private final AnswerService answerService;
    private final MemberSurveyService memberSurveyService;
    private final SurveyService surveyService;
    private final ObjectMapper mapper;

    // === 테스트용 ===

    @GetMapping("/test")
    public String testGetMember() {
        return "Test Survey";
    }

    @GetMapping("/test/jwt")
    public Member testJwtMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return member;
    }

    @GetMapping("/api/data")    //  생성한 설문지 조회
    public List<Survey> surveyManagement(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        List<Survey> allQuestionIMade = questionService.findAllSurveyIMade(member);

        return allQuestionIMade;
    }
    // === 테스트용 ===


    @PostMapping("/new")    // 설문 생성 컨트롤러
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionDto obj, Authentication authentication) {
        System.out.println("obj.questionNum = " + obj.getTitle());    // 설문 제목
        System.out.println("obj.getQuestionList = " + obj.getQuestionList());   // 객관식 - 보기 리스트
        System.out.println("obj.getVisibility = " + obj.getVisibility());   // 공개 여부
        System.out.println("obj.getWearable = " + obj.getWearable());       // 기기 착용 필수 여부
        System.out.println("obj.getSurveyId = " + obj.getSurveyId());
        System.out.println("obj.getStartDate = " + obj.getStartDate());
        System.out.println("obj.getEndDate = " + obj.getEndDate());
        List<CreateQuestionInput> questionList = obj.getQuestionList();
        //JWT 토큰에서 저장되어있는 유저 정보 가져오기
        Member member = (Member) authentication.getPrincipal();
        System.out.println("member.getId() = " + member.getId());
        System.out.println("member.getNickname() = " + member.getNickname());

        Survey newSurvey = questionService.createSurvey(obj, member);
        questionService.createQuestion(newSurvey, questionList);

        return new ResponseEntity<>(newSurvey.getId(), HttpStatus.OK);
    }


    @PatchMapping("/{survey_id}")     // 생성한 설문 수정
    public ResponseEntity<?> updateQuestion(@PathVariable("survey_id") Long survey_id,@RequestBody PatchQuestoinDTO obj,
                                            Authentication authentication,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("값을 모두 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        System.out.println("obj.questionNum = " + obj.getTitle());    // 설문 제목
        System.out.println("obj.getQuestionList = " + obj.getQuestionList());   // 객관식 - 보기 리스트
        System.out.println("obj.getSavedquestionList = " + obj.getSavedquestionList());
        System.out.println("obj.getVisibility = " + obj.getVisibility());   // 공개 여부
        System.out.println("obj.getWearable = " + obj.getWearable());       // 기기 착용 필수 여부
        System.out.println("obj.getSurveyId = " + obj.getSurveyId());

        System.out.println("obj.getNumDeleteList = " + obj.getNumDeleteList());       // 삭제할 문항 리스트

        List<CreateQuestionInput> questionList = obj.getQuestionList();
        //JWT 토큰에서 저장되어있는 유저 정보 가져오기
        Member member = (Member) authentication.getPrincipal();
        System.out.println("member.getId() = " + member.getId());
        System.out.println("member.getNickname() = " + member.getNickname());

        try {
            Survey newSurvey = questionService.createSurvey(obj, member, survey_id);
            questionService.createQuestion(newSurvey, questionList); // 수정화면에서 새로 만든 문항

            Survey updatedSurvey = questionService.update(obj, authentication, survey_id);
            questionService.delete(survey_id, obj.getNumDeleteList());
            return new ResponseEntity<>(updatedSurvey.getId(), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Survey not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace(); // add this line
            return new ResponseEntity<>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")  // 설문 삭제
    public ResponseEntity<?> deleteSurvey(
            @PathVariable(name = "id") Long id,
            Authentication authentication
    ) {
        try {

            Member member = (Member) authentication.getPrincipal();

            return surveyService.deleteSurvey(id, member);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }


    // surveyId에 해당하는 설문지의 통계 결과 가져오기
    @GetMapping("/result/{surveyId}")
    public Survey surveyStatistic(@PathVariable("surveyId") Long surveyId) {

//        Member member = (Member) authentication.getPrincipal();
        System.out.println("surveyId = " + surveyId);
        Survey questionOfSurvey = questionService.getSurveyStatistic(surveyId);

        return questionOfSurvey;
    }

}

