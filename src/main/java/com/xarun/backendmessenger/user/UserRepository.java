package com.xarun.backendmessenger.user;

import com.xarun.backendmessenger.user.userRoles.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT (u) FROM User u WHERE (u.name = :nameOrEmail AND  u.password = :password) OR (u.email = :nameOrEmail AND u.password = :password)")
    long existsByNameOrEmailAndPassword(@Param("nameOrEmail") String nameOrEmail, @Param("password") String password);

    @Query("SELECT COUNT (u) FROM User u WHERE u.userRole.roleName = :admin")
    long countAllAdmins(@Param("admin") String admin);

    @Query("SELECT DISTINCT u FROM  User u WHERE u.name = :nameOrEmail OR u.email = :nameOrEmail")
    User findFirstByNameOrEmail(@Param("nameOrEmail") String input);

    @Query("SELECT DISTINCT t.user FROM Token t WHERE t.tokenValue = :token")
    User getFirstUserByToken(@Param("token") String authentication);

    boolean existsByNameOrEmail(String name, String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByRegisterKey(String registerKey);

    @Query("SELECT uf.user FROM UserFriend uf WHERE uf.friend.userId = :id AND uf.confirmed = false")
    List<User> findAllFriendRequests(@Param("id") long userId);

    @Query("SELECT uf.friend FROM UserFriend uf WHERE uf.user.userId = :id AND uf.confirmed = false")
    List<User> findAllRequestedFriends(@Param("id") long userId);

    @Query("SELECT uf.friend FROM UserFriend uf WHERE uf.user.userId = :id AND uf.confirmed = true")
    List<User> findAllFriendsByUserId(@Param("id") long userId);

    List<User> findByNameContaining(String name);

    @Query("SELECT uf.friend FROM UserFriend uf WHERE uf.user.userId = :id AND uf.confirmed = true AND LOWER(uf.friend.name) LIKE CONCAT('%', LOWER(:name) ,'%')")
    List<User> findByNameContainingAndUserId(@Param("name") String name, @Param("id") long userId);

    List<User> findAllByUserRole(UserRole authenticatedUser);

    List<User> findByNameContainingAndUserRole(String name, UserRole authenticatedUser);

    @Query("SELECT u FROM User u WHERE u.confirmed = false")
    List<User> findAllByConfirmed();

    @Query("SELECT u FROM User u WHERE u.registerKey = :registerKey")
    User findByKey(@Param("registerKey") String registerKey);

    User findFirstByEmail(String email);

    User findByName(String name);

    User findBySessionId(String id);
}

