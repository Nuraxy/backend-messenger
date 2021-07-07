package com.xarun.backendmessenger.configuration;

import com.xarun.backendmessenger.encryption.HashService;
import com.xarun.backendmessenger.token.Token;
import com.xarun.backendmessenger.token.TokenRepository;
import com.xarun.backendmessenger.token.TokenService;
import com.xarun.backendmessenger.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class TokenInterceptor implements HandlerInterceptor {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final HashService hashService;

    public TokenInterceptor(TokenRepository tokenRepository,
                            UserRepository userRepository,
                            TokenService tokenService,
                            HashService hashService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.hashService = hashService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        } else {
            String authentication = request.getHeader("Authentication");
            if (authentication != null) {
                if (tokenRepository.getFirstByTokenValue(hashService.plainHash(authentication)) != null) {
                    Token token = tokenRepository.getFirstByTokenValue(hashService.plainHash(authentication));
                    LocalDateTime currentDate = LocalDateTime.now();
                    if (token.getUser().getUserRole().getRoleName().equals(request.getHeader("UserRole"))) {
                        if (!(handler instanceof HandlerMethod) ||
                                ((HandlerMethod) handler).getMethod().getAnnotation(NoSessionReset.class) == null) {
                            if (token.getLastRequest().plusMinutes(15).isAfter(currentDate)) {
                                tokenService.updateLastRequest(token, currentDate);
                            } else {
                                tokenService.delete(token.getTokenId());
                                returnUnauthorized();
                                return false;
                            }
                        }
                        return true;
                    } else {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "role changed");
                    }
                } else return userRepository.existsByRegisterKey(authentication);
            } else {
                returnUnauthorized();
                return false;
            }
        }
    }

    private void returnUnauthorized() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Timed Out");
    }
}
