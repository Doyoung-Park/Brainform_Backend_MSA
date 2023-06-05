package kakao.surveyservice.Controller;


import kakao.surveyservice.global.entity.Member;
import kakao.surveyservice.global.entity.Survey;
import kakao.surveyservice.global.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {

    private final QuestionService questionService;

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

}

