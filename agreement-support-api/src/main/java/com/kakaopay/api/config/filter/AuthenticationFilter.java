package com.kakaopay.api.config.filter;

import com.kakaopay.api.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final List<String> PERMIT_URIS = Arrays.asList("/v1/supports/**", "/v1/token/**");
    private static final List<String> EXCEPT_URIS = Arrays.asList("/v1/members/**");

    private String secretKey;

    public AuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (isNotPermit(requestURI, EXCEPT_URIS)) {
            if (isNotPermit(requestURI, PERMIT_URIS)) {
                responseUnAuthorized(response);
                return;
            }

            String[] authKeyParts = splitHeader(request);
            String scheme = authKeyParts.length > 1 ? authKeyParts[0] : "";
            String token = authKeyParts.length > 1 ? authKeyParts[1] : "";

            if (isInvalidAuthKey(scheme, token)) {
                responseUnAuthorized(response);
                return;
            }

            if (isNotTokenVerified(token)) {
                responseUnAuthorized(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String[] splitHeader(HttpServletRequest request) {
        Optional<String> authKey = Optional.ofNullable(request.getHeader("Authorization"));
        return authKey.map(s -> s.split(" ")).orElseGet(() -> new String[0]);
    }

    private boolean isNotPermit(String requestURI, List<String> uris) {
        boolean isPermit = false;

        for (String uri : uris) {
            if (ANT_PATH_MATCHER.match(uri, requestURI)) {
                isPermit = true;
                break;
            }
        }
        return !isPermit;
    }

    private boolean isInvalidAuthKey(String scheme, String token) {
        return StringUtils.isEmpty(token) ||
                !scheme.equals("Bearer") ||
                JwtTokenUtils.decodeToken(token).getMemberId().equals("GUEST");
    }

    private boolean isNotTokenVerified(String token) {
        return !JwtTokenUtils.verifyToken(secretKey, token);
    }

    private void responseUnAuthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"unauthorized\"}");
    }
}
