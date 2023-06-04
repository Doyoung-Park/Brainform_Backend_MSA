package kakao.memberservice.service;

import kakao.memberservice.dto.MemberRegisterDTO;
import kakao.memberservice.dto.TokenDTO;
import kakao.memberservice.entity.Member;
import kakao.memberservice.jwt.TokenProvider;
import kakao.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDTO join(MemberRegisterDTO dto, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        Member savedMember = memberRepository.save(member.register(dto));

        String accessToken = tokenProvider.createAccessToken(savedMember);
        String refreshToken = tokenProvider.createRefreshToken(savedMember);

        return new TokenDTO(accessToken, refreshToken);
    }

    public Member update(MemberRegisterDTO dto, Authentication authentication) {

        // Authentication 객체에서 사용자 이름 가져오기
        Member member = (Member) authentication.getPrincipal();

        // 사용자 이름으로 회원 정보 조회
        Member memberbyid = memberRepository.findById(member.getId())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // DTO로부터 변경할 정보를 가져와 회원 정보 업데이트
        memberbyid.setNickname(dto.getNickname());
        memberbyid.setAge(dto.getAge());
        memberbyid.setGender(dto.getGender());
        memberbyid.setJob(dto.getJob());

        // 변경된 회원 정보 저장
        return memberRepository.save(memberbyid);
    }
}
