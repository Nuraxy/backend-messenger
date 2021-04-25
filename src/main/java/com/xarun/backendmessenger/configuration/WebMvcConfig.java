package com.xarun.backendmessenger.configuration;

import com.xarun.backendmessenger.encryption.HashService;
import com.xarun.backendmessenger.token.TokenRepository;
import com.xarun.backendmessenger.token.TokenService;
import com.xarun.backendmessenger.user.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final HashService hashService;

    public WebMvcConfig(TokenRepository tokenRepository,
                        UserRepository userRepository, TokenService tokenService,
                        HashService hashService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.hashService = hashService;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenRepository, userRepository, tokenService, hashService))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/users/authenticate",
                        "/users/register",
                        "/error",
                        "/users/verify",
                        "/users/forgotPassword");
    }

}
