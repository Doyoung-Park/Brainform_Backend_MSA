package kakao.answerservice.Controller;


import kakao.answerservice.global.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @GetMapping("/test")
    public String testGetMember() {
        return "Test answer";
    }

    @GetMapping("/test/jwt")
    public Member testJwtMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return member;
    }
}

