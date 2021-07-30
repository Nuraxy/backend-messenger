package com.xarun.backendmessenger.user;

import com.xarun.backendmessenger.email.SendEmailService;
import com.xarun.backendmessenger.encryption.HashService;
import com.xarun.backendmessenger.encryption.SecureRandomService;
import com.xarun.backendmessenger.openapi.model.UpdateResource;
import com.xarun.backendmessenger.token.TokenRepository;
import com.xarun.backendmessenger.user.exception.UserNotFound;
import com.xarun.backendmessenger.user.userRoles.UserRole;
import com.xarun.backendmessenger.user.userRoles.UserRoleRepository;
import com.xarun.backendmessenger.webSocket.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final HashService hashService;
    private final UserFriendRepository userFriendRepository;
    private final SecureRandomService secureRandomService;
    private final UserRoleRepository userRoleRepository;
    private final SendEmailService sendEmailService;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            HashService hashService,
            UserFriendRepository userFriendRepository,
            SecureRandomService secureRandomService,
            UserRoleRepository userRoleRepository,
            SendEmailService sendEmailService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.hashService = hashService;
        this.userFriendRepository = userFriendRepository;
        this.secureRandomService = secureRandomService;
        this.userRoleRepository = userRoleRepository;
        this.sendEmailService = sendEmailService;
        this.userMapper = userMapper;
    }

    public User registerUser(User userToSave) {
        //Todo zeit in key
        LocalDateTime currentDate = LocalDateTime.now();
        LocalTime currentTime = LocalTime.now();
        String keyUUID = (UUID.randomUUID().toString() + "--" + currentTime);
        byte[] bytes = secureRandomService.getBytes();
        userToSave.setRegisterKey(keyUUID);
        userToSave.setSalt(bytes);
        userToSave.setRegistrationDate(currentDate);
        String hashedPw = hashService.hashPassword(userToSave.getSalt(), userToSave.getPassword());
        userToSave.setPassword(hashedPw);
        return userRepository.save(userToSave);
    }

    public Boolean existsByNameOrEmailAndPassword(String input, String password) {
        User user = userRepository.findFirstByNameOrEmail(input);
        final String hashedPassword = hashService.hashPassword(user.getSalt(), password);
        return userRepository.existsByNameOrEmailAndPassword(input, hashedPassword) > 0;
    }

    public Boolean enoughAdmins() {
        String admin = "Admin";
        return userRepository.countAllAdmins(admin) > 1;
    }

    public void delete(long userId) {
        tokenRepository.deleteAllByUserUserId(userId);
        userFriendRepository.deleteByUserUserId(userId);
        userFriendRepository.deleteByFriendUserId(userId);
        userRepository.deleteById(userId);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public List<User> findAllPublic() {
        long publicId = 2;
        UserRole authenticatedUser = userRoleRepository.findById(publicId).orElseThrow(UserNotFound::new);
        return this.userRepository.findAllByUserRole(authenticatedUser);
    }

    public List<User> findByNameContainingAndUserRole(String name) {
        long publicId = 2;
        UserRole authenticatedUser = userRoleRepository.findById(publicId).orElseThrow(UserNotFound::new);
        return userRepository.findByNameContainingAndUserRole(name, authenticatedUser);
    }

    public User updateUserPassword(User currentUser, String newPassword) {
        byte[] bytes = secureRandomService.getBytes();
        updateKey(currentUser);
        currentUser.setSalt(bytes);
        currentUser.setPassword(hashService.hashPassword(bytes, newPassword));
        return userRepository.save(currentUser);
    }

    public User findById(long id) {
        return this.userRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    public User updateProfile(Long userId,
                              UpdateResource body,
                              boolean allowAdmin) {
        User currentUser = findById(userId);
        currentUser.setName(body.getName());
        currentUser.setEmail(body.getEmail());
        currentUser.setPublicKey(body.getPublicKey());
        if (body.getConfirmed()) {
            currentUser.setConfirmed(body.getConfirmed());
        }
        UserRole userRoleAdmin = userRoleRepository.findByRoleName("Admin").orElseThrow(UserNotFound::new);
        UserRole currentUserRole = userRoleRepository.findByRoleName(body.getUserRole().getRoleName()).orElseThrow(UserNotFound::new);
        // input role nicht Admin und currentUser = Admin
        if (!body.getUserRole().getRoleName().equals(userRoleAdmin.getRoleName()) && currentUser.getUserRole().getRoleName().equals(userRoleAdmin.getRoleName())) {
            if (enoughAdmins()) {
                currentUser.setUserRole(currentUserRole);
            }
            //wenn Admin user zu Admin macht
        } else if (body.getUserRole().getRoleName().equals("Admin") && allowAdmin) {
            currentUser.setUserRole(currentUserRole);
            //wenn norm User seine Rolle ändert
        } else if (!body.getUserRole().getRoleName().equals("Admin") && !currentUser.getUserRole().getRoleName().equals("Admin")) {
            currentUser.setUserRole(currentUserRole);
        }
        return getSave(currentUser);
    }

    private User getSave(User currentUser) {
        return userRepository.save(currentUser);
    }

    public User getFirstUserByToken(String authentication) {
        return userRepository.getFirstUserByToken(authentication);
    }

    public boolean nameOrEmailExists(String name,
                                     String email) {
        return userRepository.existsByNameOrEmail(name, email);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllFriendsByUserId(long userId) {
        return userRepository.findAllFriendsByUserId(userId);
    }

    public boolean existsDirection(long userId, long friendId) {
        return userFriendRepository.existsByUserUserIdAndFriendUserId(userId, friendId);
    }

    public void requestFriends(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        UserFriend userFriendRelationship = new UserFriend();
        if (!userFriendRepository.existsByUserUserIdAndFriendUserId(userId, friendId)) {
            if (userFriendRepository.existsByUserUserIdAndFriendUserId(friendId, userId)) {
                UserFriend entry = userFriendRepository.findByUserUserIdAndFriendUserId(friendId, userId);
                entry.setConfirmed(true);
                userFriendRepository.save(entry);
                userFriendRelationship.setConfirmed(true);
                friendRequestAccepted(friend);
            } else {
                userFriendRelationship.setConfirmed(false);
                newFriendRequest(friend);
            }
            userFriendRelationship.setUser(user);
            userFriendRelationship.setFriend(friend);
            userFriendRepository.save(userFriendRelationship);
        }
    }

    @Async
    void newFriendRequest(User friend){
//        sendEmailService.newFriendRequest(friend);
    }

    public void updateFriendConfirmed(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        UserFriend entry = userFriendRepository.findByUserAndFriend(friend, user);
        entry.setConfirmed(true);
        userFriendRepository.save(entry);
        friendRequestAccepted(friend);

        UserFriend reverseEntry = new UserFriend();
        reverseEntry.setUser(user);
        reverseEntry.setFriend(friend);
        reverseEntry.setConfirmed(true);
        userFriendRepository.save(reverseEntry);
    }

    @Async
    void friendRequestAccepted(User friend){
//        sendEmailService.friendRequestAccepted(friend);
    }

    public void deleteByUserAndFriend(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        userFriendRepository.deleteByUserAndFriend(user, friend);
        userFriendRepository.deleteByFriendAndUser(user, friend);
    }

    public List<User> findAllFriendRequests(long userId) {
        return userRepository.findAllFriendRequests(userId);
    }

    public List<User> findAllRequestedFriends(long userId) {
        return userRepository.findAllRequestedFriends(userId);
    }

    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    public List<User> findByNameContainingAndUserId(long userId, String name) {
        return userRepository.findByNameContainingAndUserId(name, userId);
    }

    public List<User> findAllByConfirmed() {
        return userRepository.findAllByConfirmed();
    }

    public User findFirstByNameOrEmail(String nameOrEmail) {
        return userRepository.findFirstByNameOrEmail(nameOrEmail);
    }

    public User getFirstUserByKey(String registerKey) {
        return userRepository.findByKey(registerKey);
    }

    public boolean existByKey(String registerKey) {
        return userRepository.existsByRegisterKey(registerKey);
    }

    public User findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public User updateKey(User user) {
        String keyUUID = UUID.randomUUID().toString();
        LocalTime currentDateTime = LocalTime.now();
        user.setRegisterKey(keyUUID + "--" + currentDateTime);
        return userRepository.save(user);
    }

//    public void setLoginMode(LoginResource body) {
//        User currentUser = findFirstByNameOrEmail(body.getNameOrEmail());
//        currentUser.setLoginMode(body.getLoginMode() != null);
//    }

    //websocket send message to every user also Admins
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public void greeting(Message message, String sessionId) {
        User user = findById(message.getSenderId());
        user.setSessionId(sessionId);
        user.setPublicKey(message.getMessage());
        userRepository.save(user);
    }

    public void removeSession(String id) {
        User user = userRepository.findBySessionId(id);
        user.setSessionId(null);
        userRepository.save(user);
    }
}
