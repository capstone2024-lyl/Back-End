package capstone.capstone2024.global.auth;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final String secretKey;
    private final String googleTokenInfoUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String googleToken = request.getHeader("X-Google-Token");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            // JWT 토큰 처리
            String token = authorization.split(" ")[1].trim();
            processJwtToken(token, request, response, filterChain);
        }

        if (googleToken != null) {
            try {
                processGoogleAccessToken(googleToken);
            } catch (GeneralSecurityException e) {
                throw new ServletException("Google Token verification failed", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void processJwtToken(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (JwtTokenUtil.isExpired(token, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }

            String loginId = JwtTokenUtil.getLoginId(token, secretKey);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginId, null);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
        }
    }

    private void processGoogleAccessToken(String googleAccessToken) throws GeneralSecurityException {
        WebClient webClient = WebClient.create();
        String response = webClient.get()
                .uri(googleTokenInfoUrl + "?access_token=" + googleAccessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null || response.contains("error")) {
            throw new BadRequestException(ROW_DOES_NOT_EXIST, "Invalid ID token.");
        }
    }
}
