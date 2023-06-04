package kakao.surveyservice.Controller;


import kakao.surveyservice.global.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @GetMapping("/test")
    public String testGetMember() {
        return "Test Survey";
    }

    @GetMapping("/test/jwt")
    public Member testJwtMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return member;
    }
}

