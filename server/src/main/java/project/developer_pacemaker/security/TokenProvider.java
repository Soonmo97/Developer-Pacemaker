package project.developer_pacemaker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.developer_pacemaker.config.jwt.JwtProperties;
import project.developer_pacemaker.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {
    final private JwtProperties jwtProperties;
    @Autowired
    public TokenProvider(final JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String create(UserEntity user) {
        Date expiredDate
            = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()) // 암호화 방식
            .setSubject(String.valueOf(user.getUSeq())) // 사용자 식별
            .setIssuer(jwtProperties.getIssuer()) // 토큰 발급자 식별
            .setExpiration(expiredDate) // 만료
            .setIssuedAt(new Date()) // 언제 만들어졌는지
            .compact();
    }

    // 입력된 token 에서 payload 에 있는 userId 뽑기
    public String validateAndGetUSeq(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token) // 토큰이 위조되지 않았다면 payload 를 return
            .getBody();

        return claims.getSubject();
    }
}
