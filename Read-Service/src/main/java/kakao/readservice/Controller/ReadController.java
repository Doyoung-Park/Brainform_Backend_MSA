package kakao.readservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakao.readservice.global.dto.FilterDTO;
import kakao.readservice.global.dto.MemberRegisterDTO;
import kakao.readservice.global.dto.TokenDTO;
import kakao.readservice.global.entity.BrainData;
import kakao.readservice.global.entity.BrainMemberInfo;
import kakao.readservice.global.entity.Member;
import kakao.readservice.global.entity.Survey;
import kakao.readservice.global.repository.BrainWaveCodeRepository;
import kakao.readservice.global.repository.MemberRepository;
import kakao.readservice.global.service.MemberService;
import kakao.readservice.global.service.MemberSurveyService;
import kakao.readservice.global.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/read")
public class ReadController {


    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BrainWaveCodeRepository brainWaveCodeRepository;
    private final QuestionService questionService;
    private final ObjectMapper mapper;
    private final MemberSurveyService memberSurveyService;

    // === 테스트용 ===
    @GetMapping("/test")
    public String testGetMember() {
        return "Test Member";
    }


    @GetMapping("/test/jwt")
    public Member testJwtMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return member;
    }
    // === 테스트용 ===


    /**
     * 뇌파를 측정하면서 설문 응답중인지 체크
     * @param code: 뇌파 측정 코드
     * @return 뇌파 측정 코드, Survey ID, Member ID, flag
     */
    @GetMapping("/answer/userInfo/{code}") // C# 에서 응답 전에 flag값 확인하는 요청
    public BrainMemberInfo sendMemberInfo(@PathVariable(name = "code") String code) {

        BrainMemberInfo brainMemberInfo = brainWaveCodeRepository.findByCode(code).get();

        return brainMemberInfo;
    }

    @GetMapping("/member/") // 내 정보 조회
    public Member getMember(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> data = new HashMap<String, Object>();
        Member member = (Member) authentication.getPrincipal();
        System.out.println(member);

        return member;
    }

    // survey_id에 해당하는 설문지의 질문들 가져오는 컨트롤러
    @GetMapping("/survey/question/{survey_id}") // 해당 설문지의 질문들 조회
    public Survey findQuestionById(@PathVariable("survey_id") Long survey_id) {
        Survey responseSurvey = questionService.findQuestionById(survey_id);
        return responseSurvey;
//        if (!responseSurvey.equals(null)) {
//            return new ResponseEntity<>(responseSurvey, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
//        }
    }


    @GetMapping("/survey/created")    //  생성한 설문지 조회
    public List<Survey> surveyManagement(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        List<Survey> allQuestionIMade = questionService.findAllSurveyIMade(member);

        return allQuestionIMade;
    }


    @GetMapping("/survey/answered")   // 응답한 설문지 조회
    public List<Survey> ManagementAnsweredSurveyList(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        List<Survey> allSurveyIAnswered = questionService.findAllSurveyIAnswered(member);

        return allSurveyIAnswered;
    }

    @GetMapping("/survey/result/{surveyId}")
    public Survey surveyStatistic(@PathVariable("surveyId") Long surveyId) {

//        Member member = (Member) authentication.getPrincipal();
        System.out.println("surveyId = " + surveyId);
        Survey questionOfSurvey = questionService.getSurveyStatistic(surveyId);

        return questionOfSurvey;
    }

    @GetMapping("/survey/filter")
    public Survey GetSurveyDataWithFilter(
            @RequestParam(value = "id", required = true) String surveyId,
            @RequestParam(value = "gender", required = false) List<String> genders,
            @RequestParam(value = "age", required = false) List<String> ages,
            @RequestParam(value = "job", required = false) List<String> jobs,
            @RequestParam(value = "isActive", required = false) String active) throws ChangeSetPersister.NotFoundException, JsonProcessingException {


        log.info("filter요청 들어옴");
        log.info("활성화 요청 파라미터={}", active);
        FilterDTO filterDTO = new FilterDTO(Long.parseLong(surveyId), genders, ages, jobs, active);
        log.info(mapper.writeValueAsString(filterDTO));
        Survey dataWithFilter = memberSurveyService.getDataWithFilter(filterDTO);
        log.info("resposne={}", mapper.writeValueAsString(dataWithFilter));

        return dataWithFilter;
    }
}
