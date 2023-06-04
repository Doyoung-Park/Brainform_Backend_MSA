//package kakao.gateway.jwt;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import kakao.gateway.global.repository.RefreshTokenRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.Optional;
//
//
//@Component
//@Slf4j
//public class TokenProvider implements InitializingBean {
//    private static final String AUTHORITIES_KEY = "auth";
//
//    private final String secret;
//    private final long tokenValidityInMilliseconds;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//
//    private final MemberRepository memberRepository;
//    private Key key;
//
//    public TokenProvider (
//            @Value("${jwt.secret}") String secret,
//            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds, RefreshTokenRepository refreshTokenRepository,
//            MemberRepository memberRepository) {
//        this.secret = secret;
//        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    @Transactional
//    public String createAccessToken(Member member) {
//
//        long now = (new Date()).getTime();
//        Date validity = new Date(now + this.tokenValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setSubject(String.valueOf(member.getId()))
//                .claim("member", member)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .setExpiration(validity)
//                .compact();
//    }
//
//    public String createRefreshToken(Member member) {
//
//        long now = (new Date()).getTime();
//        Date validity = new Date(now + this.tokenValidityInMilliseconds);
//
//        String token = Jwts.builder()
//                .setSubject(String.valueOf(member.getId()))
//                .claim("email", member.getEmail())
//                .signWith(key, SignatureAlgorithm.HS512)
//                .setExpiration(validity)
//                .compact();
//
//        RefreshToken refreshToken = new RefreshToken(member.getId(), token);
//
//        //refreshTokenRepository.save(refreshToken);
//
//        return token;
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//
//        Optional<Member> member = memberRepository.findById(Long.valueOf(claims.getSubject()));
//        return new UsernamePasswordAuthenticationToken(member.get(),token);
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//            log.info("만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.info("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//            log.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return false;
//    }
//}
//
