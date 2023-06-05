package kakao.readservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakao.readservice.global.dto.MemberRegisterDTO;
import kakao.readservice.global.dto.TokenDTO;
import kakao.readservice.global.entity.BrainData;
import kakao.readservice.global.entity.BrainMemberInfo;
import kakao.readservice.global.entity.Member;
import kakao.readservice.global.repository.BrainWaveCodeRepository;
import kakao.readservice.global.repository.MemberRepository;
import kakao.readservice.global.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
public class ReadController {


    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BrainWaveCodeRepository brainWaveCodeRepository;

    private final ObjectMapper objectMapper;

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


    @SneakyThrows
    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody @Validated MemberRegisterDTO dto,
                                      Authentication authentication,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("값을 모두 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        TokenDTO token = memberService.join(dto, authentication);

        return new ResponseEntity<>(token.getAccessToken(), HttpStatus.OK);
    }

    @PatchMapping("/api/patchmember")
    public ResponseEntity<?> updateMember(@RequestBody @Validated MemberRegisterDTO dto,
                                          Authentication authentication,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("값을 모두 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        try {
            Member updatedMember = memberService.update(dto, authentication);

            if (updatedMember != null) {
                return new ResponseEntity<>(updatedMember, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("회원 정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/api/members")
    public Member getMember(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> data = new HashMap<String, Object>();
        Member member = (Member) authentication.getPrincipal();
        System.out.println(member);

        return member;
    }


    /*
     * 설문 응답하기 전에 입력할 코드 세션에 저장
     * */
    @PostMapping("/api/{id}/{code}")
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

    @GetMapping("/api/userInfo/{code}")
    public BrainMemberInfo sendMemberInfo(@PathVariable(name = "code") String code,
                                          HttpServletRequest request) throws JsonProcessingException {

        //log.info(code);
        BrainMemberInfo brainMemberInfo = brainWaveCodeRepository.findByCode(code);
        String s = objectMapper.writeValueAsString(brainMemberInfo);
        //log.info(s);

        return brainMemberInfo;
    }

    @PostMapping("api/{id}/{code}/stop")
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

        return new ResponseEntity<>("설문 종료", HttpStatus.OK);
    }

    @PostMapping("api/imgInfo")
    public BrainData postBrainData(@RequestParam("braindata") String brainData,
                                   @RequestParam("image") MultipartFile image) throws IOException {

        System.out.printf("요청 받음" + brainData);
        // byte 배열로 이미지 데이터 변환
        byte[] imageData = image.getBytes();
        BrainData brainDataObj = new ObjectMapper().readValue(brainData, BrainData.class);

        // BrainData 객체의 필드 값을 추출하여 변수에 저장
        String memberID = brainDataObj.getMemberId();
        String surveyId = brainDataObj.getSurveyId();
        double avgAtt = brainDataObj.getAvgAtt();
        double avgMed = brainDataObj.getAvgMed();

        // BrainData 객체 생성
        BrainData newBrainDataObj = BrainData.builder()
                .memberId(memberID)
                .surveyId(surveyId)
                .image(imageData)
                .avgAtt(avgAtt)
                .avgMed(avgMed)
                .build();

        byte[] img = newBrainDataObj.getImage();
        String filename = "C:\\Users\\USER\\Desktop\\" + memberID + "_" + surveyId + ".png";
        // 바이트 배열로부터 BufferedImage 객체 생성
        InputStream in = new ByteArrayInputStream(img);
        BufferedImage bufferedImage = ImageIO.read(in);

        // BufferedImage 객체를 PNG 파일로 저장
        File outputfile = new File(filename);
        ImageIO.write(bufferedImage, "png", outputfile);
        return newBrainDataObj;
    }
}
