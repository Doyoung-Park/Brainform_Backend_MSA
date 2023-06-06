package kakao.answerservice.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakao.answerservice.global.controller.ChatController;
import kakao.answerservice.global.dto.BrainDataDTO;
import kakao.answerservice.global.dto.CreateAnswerDto;
import kakao.answerservice.global.entity.BrainMemberInfo;
import kakao.answerservice.global.entity.Member;
import kakao.answerservice.global.entity.MemberSurvey;
import kakao.answerservice.global.repository.BrainWaveCodeRepository;
import kakao.answerservice.global.service.AnswerService;
import kakao.answerservice.global.service.BrainWaveService;
import kakao.answerservice.global.service.MemberSurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final MemberSurveyService memberSurveyService;
    private final BrainWaveCodeRepository brainWaveCodeRepository;
    private final ChatController chatController;
    private final BrainWaveService brainWaveService;

    @GetMapping("/test")
    public String testGetMember() {
        return "Test answer";
    }

    @GetMapping("/test/jwt")
    public Member testJwtMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return member;
    }


    @PostMapping("/finished")     // 응답하기
    public ResponseEntity<?> createAns(@RequestBody CreateAnswerDto obj, Authentication authentication) {

        System.out.println("obj.surveyId = " + obj.getSurveyId());    // 설문 id
        System.out.println("obj.answer = " + obj.getAnswers());   // 객관식 - 보기 리스트

        ArrayList questionList = obj.getAnswers();
        Long surveyId = obj.getSurveyId();
        Member member = (Member) authentication.getPrincipal();

        MemberSurvey memberSurvey = memberSurveyService.createMemberSurvey(member, surveyId);
        answerService.createAns(questionList, memberSurvey); // Change this line

        return new ResponseEntity<>("설문 응답이 제출되었습니다.", HttpStatus.OK);

    }

    @PostMapping("/{id}/{code}")    // 설문 응답 전에 뇌파 코드 입력
    public ResponseEntity<?> getBrainCode(@PathVariable(name = "code") String code,
                                          @PathVariable(name = "id") Long surveyId,
                                          Authentication authentication) {

        //JWT 토큰에서 저장되어있는 유저 정보 가져오기
        Member member = (Member) authentication.getPrincipal();


        BrainMemberInfo brainMemberInfo = BrainMemberInfo.builder()
                .code(code)
                .surveyId(surveyId)
                .memberId(member.getId())
                .flag(true)
                .build();

        brainWaveCodeRepository.save(brainMemberInfo);

        return new ResponseEntity<>("설문을 시작해주세요", HttpStatus.OK);
    }

    @PostMapping("/{id}/{code}/stop")    // 종료 버튼 눌렀을 때 응답 완료
    public ResponseEntity<?> stopBrain(@PathVariable(name = "code") String code,
                                       @PathVariable(name = "id") Long surveyId,
                                       Authentication authentication,
                                       HttpServletRequest request) {

        //JWT 토큰에서 저장되어있는 유저 정보 가져오기
        Member member = (Member) authentication.getPrincipal();


        BrainMemberInfo brainMemberInfo = BrainMemberInfo.builder()
                .code(code)
                .surveyId(surveyId)
                .memberId(member.getId())
                .flag(false)
                .build();

        brainWaveCodeRepository.save(brainMemberInfo);

        log.info("뇌파 측정 종료");
        return new ResponseEntity<>("설문 종료", HttpStatus.OK);
    }
    @PostMapping("/brainwave-info")     // 응답 완료 후 뇌파 데이터 & 사진 데이터 전송
    public BrainDataDTO postBrainData(@RequestParam("braindata") String brainData,
                                      @RequestParam("image") MultipartFile image) throws IOException {

        log.info("뇌파 이미지={}", image.getOriginalFilename());
        BrainDataDTO brainDataDTO = new ObjectMapper().readValue(brainData, BrainDataDTO.class);
        brainDataDTO.setImage(image);
        log.info("뇌파 이미지={}", brainDataDTO.getImage().getOriginalFilename());
        ResponseEntity<?> response = chatController.sendFinished(); // 소켓으로 완료 요청 보냄
        System.out.println("response 내용: " + response);
        brainWaveService.saveBrainWave(brainDataDTO);

        return brainDataDTO;
    }

}

