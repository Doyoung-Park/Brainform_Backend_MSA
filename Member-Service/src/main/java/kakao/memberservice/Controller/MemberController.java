package kakao.memberservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakao.memberservice.global.dto.MemberRegisterDTO;
import kakao.memberservice.global.dto.TokenDTO;
import kakao.memberservice.global.entity.BrainData;
import kakao.memberservice.global.entity.BrainMemberInfo;
import kakao.memberservice.global.entity.Member;
import kakao.memberservice.global.repository.BrainWaveCodeRepository;
import kakao.memberservice.global.repository.MemberRepository;
import kakao.memberservice.global.service.MemberService;
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
public class MemberController {


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
    @PostMapping("/register")    // 회원가입
    public ResponseEntity<?> register(@RequestBody @Validated MemberRegisterDTO dto,
                                      Authentication authentication,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("값을 모두 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        TokenDTO token = memberService.join(dto, authentication);

        return new ResponseEntity<>(token.getAccessToken(), HttpStatus.OK);
    }

    @PatchMapping("/changed/")   // 회원 정보 수정
    public ResponseEntity<?> updateMember(@RequestBody @Validated MemberRegisterDTO dto,
                                          Authentication authentication,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("값을 모두 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        try {
            Member updatedMember = memberService.update(dto, authentication);

            if(updatedMember != null) {
                return new ResponseEntity<>(updatedMember, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("회원 정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch(Exception e) {
            return new ResponseEntity<>("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

