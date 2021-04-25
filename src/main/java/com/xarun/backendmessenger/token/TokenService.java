package com.xarun.backendmessenger.token;

import com.xarun.backendmessenger.encryption.HashService;
import com.xarun.backendmessenger.openapi.model.LoginResource;
import com.xarun.backendmessenger.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final HashService hashService;

    public TokenService (TokenRepository tokenRepository,
                         UserRepository userRepository,
                         HashService hashService){
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    public void delete(long tokenId) {
        tokenRepository.deleteById(tokenId);
    }

    public Token generateToken(LoginResource body) {
        final Token token = new Token();
        LocalDateTime currentDate = LocalDateTime.now();
        String value = UUID.randomUUID().toString();
        token.setTokenValue(value);
        token.setLastRequest(currentDate);
        token.setUser(userRepository.findFirstByNameOrEmail(body.getNameOrEmail()));
        return this.tokenRepository.save(token);
    }

    public Token hashGenerateToken(Token token){
        token.setTokenValue(hashService.plainHash(token.getTokenValue()));
        return this.tokenRepository.save(token);
    }

    public Token getFirstByTokenValue(String authentication) {
        return tokenRepository.getFirstByTokenValue(authentication);
    }

    public void updateLastRequest(Token token, LocalDateTime currentDate) {
        token.setLastRequest(currentDate);
        tokenRepository.save(token);
    }
}
