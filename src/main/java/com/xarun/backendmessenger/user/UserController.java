package com.xarun.backendmessenger.user;

import com.xarun.backendmessenger.configuration.NoSessionReset;
import com.xarun.backendmessenger.email.SendEmailService;
import com.xarun.backendmessenger.encryption.HashService;
import com.xarun.backendmessenger.openapi.api.UserApi;
import com.xarun.backendmessenger.openapi.model.*;
import com.xarun.backendmessenger.token.Token;
import com.xarun.backendmessenger.token.TokenMapper;
import com.xarun.backendmessenger.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"*"})
public class UserController implements UserApi {

    private final UserMapper userMapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final TokenMapper tokenMapper;
    private final HashService hashService;
    private final SendEmailService sendEmailService;
    final String adminRole = "Admin";
    final String publicRole = "Public";

    public UserController(UserMapper userMapper,
                          UserService userService,
                          TokenService tokenService,
                          TokenMapper tokenMapper,
                          HashService hashService,
                          SendEmailService sendEmailService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenMapper = tokenMapper;
        this.hashService = hashService;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public ResponseEntity<UserResource> registerUser(@Valid @NotNull UserResource body) {
        User userToSave = userMapper.mapToUserDomain(body);
        if (body.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kein Passwort vorhanden");
        } else {
            if (userService.nameOrEmailExists(body.getName(), body.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name oder E-mail existiert bereits");
            } else {
                final User newUser = userService.registerUser(userToSave);
                registerEmail(userToSave);
                final UserResource userResource = userMapper.mapToResource(newUser);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userResource);
            }
        }
    }

    @Async
    void registerEmail(User userToSave) {
        sendEmailService.registrationEmail(userToSave);
    }

    @Override
    public ResponseEntity<TokenResource> authenticateUser(LoginResource body) {
        if (userService.existsByNameOrEmailAndPassword(body.getNameOrEmail(), body.getPassword())) {
            User currentUser = userService.findFirstByNameOrEmail(body.getNameOrEmail());
            if (currentUser.isConfirmed()) {
//                userService.setLoginMode(body);
                final Token token = tokenService.generateToken(body);
                final TokenResource tokenResource = tokenMapper.mapToResource(token);
                this.tokenService.hashGenerateToken(token);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(tokenResource);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email muss bestätigt werden");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falscher Benutzername/Email oder Passwort");
        }
    }

    @Override
    public ResponseEntity<Void> forgotPassword(@Valid @NotNull ForgotPassword body) {
        User user = userService.findFirstByEmail(body.getEmail());
        User updatedUser = userService.updateKey(user);
        forgotPassword(updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Async
    void forgotPassword(User updatedUser) {
        sendEmailService.forgotPassword(updatedUser);
    }

    @Override
    public ResponseEntity<UserResource> getUserByKey(@NotNull String registerKey) {
        if (userService.existByKey(registerKey)) {
            User user = userService.getFirstUserByKey(registerKey);
            final UserResource userResource = userMapper.mapToResource(user);
            return ResponseEntity.ok(userResource);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falscher Benutzer");
        }
    }

    @Override
    public ResponseEntity<List<UserResource>> getAllUsers(String name, @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        if (currentUser.getUserRole().getRoleName().equals(adminRole)) {
            final List<User> users;
            if (name == null || name.isEmpty()) {
                users = this.userService.findAll();
            } else {
                users = this.userService.findByNameContaining(name);
            }
            final List<UserResource> userResourceList = users
                    .stream()
                    .map(userMapper::mapToResource)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResourceList);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<List<UserResource>> getAllPublicUsers(String name, @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        if (currentUser.getUserRole().getRoleName().equals(publicRole)) {
            final List<User> users;
            if (name == null || name.isEmpty()) {
                users = this.userService.findAllPublic();
            } else {
                users = this.userService.findByNameContainingAndUserRole(name);
            }
            final List<UserResource> userResourceList = users
                    .stream()
                    .map(userMapper::mapToResource)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResourceList);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<UserResource> getUserById(@NotNull String authentication, @NotNull Long userId) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        if (currentUser != null) {
            final User user = userService.findById(userId);
            final UserResource userResource = userMapper.mapToResource(user);
            return ResponseEntity.ok(userResource);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<UserResource> updateUser(@NotNull Long userId,
                                                   @NotNull String authentication,
                                                   @Valid @NotNull UpdateResource body) {
        User currentUser;
        if (userService.getFirstUserByToken(hashService.plainHash(authentication)) != null) {
            currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        } else {
            currentUser = userService.getFirstUserByKey(authentication);
        }
        long currentUserId;
        boolean allowAdmin = false;
        if (currentUser.getUserRole().getRoleName().equals(adminRole)) {
            currentUserId = userId;
            currentUser = userService.findById(currentUserId);
            allowAdmin = true;
        } else {
            currentUserId = currentUser.getUserId();
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            if (!userService.existsByName(body.getName()) || currentUser.getName().equals(body.getName()) || allowAdmin) {
                if (!userService.existsByEmail(body.getEmail()) || currentUser.getEmail().equals(body.getEmail()) || allowAdmin) {
                    currentUser = userService.updateProfile(userId, body, allowAdmin);
                    return ResponseEntity.ok(userMapper.mapToResource(currentUser));
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(@NotNull Long userId,
                                           @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId;
        if (currentUser.getUserRole().getRoleName().equals(adminRole)) {
            if (userService.enoughAdmins()) {
                currentUserId = userId;
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            currentUserId = currentUser.getUserId();
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            userService.delete(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<UserResource>> getFriendsByUserId(@NotNull Long userId,
                                                                 String name,
                                                                 @NotNull String authentication) {
        final List<User> users;
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId = currentUser.getUserId();
        if (currentUser.getUserRole().getRoleName().equals(adminRole) && currentUser.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (currentUser.getUserRole().getRoleName().equals(adminRole) && !currentUser.getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (name == null || name.isEmpty()) {
            users = userService.findAllFriendsByUserId(userId);
        } else {
            users = this.userService.findByNameContainingAndUserId(userId, name);
        }
        final List<UserResource> userResourceList = users
                .stream()
                .map(userMapper::mapToResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResourceList);
    }

    @Override
    @NoSessionReset
    public ResponseEntity<List<UserResource>> getFriendRequestByUserId(@NotNull Long userId,
                                                                       @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId = currentUser.getUserId();
        if (currentUser.getUserRole().getRoleName().equals(adminRole) && currentUser.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (currentUser.getUserRole().getRoleName().equals(adminRole) && !currentUser.getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            final List<User> users = userService.findAllFriendRequests(userId);
            final List<UserResource> userResourceList = users
                    .stream()
                    .map(userMapper::mapToResource)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResourceList);
        }
    }

    @Override
    public ResponseEntity<List<UserResource>> findAllRequestedFriendsByUserId(@NotNull Long userId,
                                                                              @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId = currentUser.getUserId();
        if (currentUser.getUserRole().getRoleName().equals(adminRole) && currentUser.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (currentUser.getUserRole().getRoleName().equals(adminRole) && !currentUser.getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            final List<User> users = userService.findAllRequestedFriends(userId);
            final List<UserResource> userResourceList = users
                    .stream()
                    .map(userMapper::mapToResource)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResourceList);
        }
    }

    @Override
    public ResponseEntity<UserResource> addFriendToUser(@NotNull Long userId,
                                                        @NotNull Long friendId,
                                                        @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId = currentUser.getUserId();
        if (currentUser.getUserRole().getRoleName().equals(adminRole) && currentUser.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (currentUser.getUserRole().getRoleName().equals(adminRole) && !currentUser.getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!userService.existsDirection(userId, friendId) && !userId.equals(friendId)) {
            userService.requestFriends(userId, friendId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> removeFriendById(@NotNull Long userId,
                                                 @NotNull Long friendId,
                                                 @NotNull String authentication) {
        Token currentToken = tokenService.getFirstByTokenValue(hashService.plainHash(authentication));
        long currentUserId = currentToken.getUser().getUserId();
        if (currentToken.getUser().getUserRole().getRoleName().equals(adminRole) && !currentToken.getUser().getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            userService.deleteByUserAndFriend(userId, friendId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<UserResource> confirmFriendsByUserIdAndFriendId(@NotNull Long userId,
                                                                          @NotNull Long friendId,
                                                                          @NotNull Boolean confirmed,
                                                                          @NotNull String authentication) {
        User currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        long currentUserId = currentUser.getUserId();
        if (currentUser.getUserRole().getRoleName().equals(adminRole) && currentUser.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (currentUser.getUserRole().getRoleName().equals(adminRole) && !currentUser.getUserId().equals(userId)) {
            currentUserId = userId;
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (confirmed) {
            userService.updateFriendConfirmed(userId, friendId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            userService.deleteByUserAndFriend(userId, friendId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<UserResource> updateUserPassword(@NotNull Long userId,
                                                           @NotNull String authentication,
                                                           @Valid @NotNull PasswordResource body) {
        User currentUser;
        boolean passwordReset = false;
        if (userService.existByKey(authentication)) {
            currentUser = userService.getFirstUserByKey(authentication);
            passwordReset = true;
        } else {
            currentUser = userService.getFirstUserByToken(hashService.plainHash(authentication));
        }
        long currentUserId = currentUser.getUserId();
        if ((currentUser.getUserRole().getRoleName().equals(adminRole) && currentUserId != userId) || passwordReset) {
            currentUser = userService.findById(userId);
            currentUser = userService.updateUserPassword(currentUser, body.getNewPassword());
            return ResponseEntity.ok(userMapper.mapToResource(currentUser));
        }
        if (userId != currentUserId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            if (!currentUser.getPassword().equals(hashService.hashPassword(currentUser.getSalt(), body.getPassword()))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                currentUser = userService.updateUserPassword(currentUser, body.getNewPassword());
                return ResponseEntity.ok(userMapper.mapToResource(currentUser));
            }
        }
    }

    @Override
    public ResponseEntity<Void> logoutUser(@NotNull Long tokenId,
                                           @NotNull String authentication) {
        Token currentToken = tokenService.getFirstByTokenValue(hashService.plainHash(authentication));
        long currentTokenId = currentToken.getTokenId();
        if (tokenId != currentTokenId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            tokenService.delete(tokenId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
