package project.developer_pacemaker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // (1) 요청의 Header 에 담겨온 token 을 뽑아서 유효한지 확인
            String token = parseBearerToken(request);
            log.warn("filter token check {}", token);


            if (token != null && !token.equalsIgnoreCase("null")) {
                String uSeq = tokenProvider.validateAndGetUserId(token);

                // (2) securityContextHolder 에 UserNamePasswordAuthToken 만들어서 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(uSeq, null, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.error("auth check error {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    public String parseBearerToken(HttpServletRequest request) {
        // 요청의 header 의 bearer 토큰에서 jwt 토큰을 뽑아오는 작업
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            // StringUtils.hasText(bearerToken): null, 공백이 아닌지
            // "Bearer abcdefg" -> "abcdefg"
            return bearerToken.substring(7);
        }

        return null;
    }
}
