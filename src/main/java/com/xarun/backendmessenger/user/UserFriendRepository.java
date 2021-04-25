package com.xarun.backendmessenger.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    UserFriend findByUserAndFriend (User friend, User user);

    void deleteByUserAndFriend(User user, User friend);
    void deleteByFriendAndUser(User user, User friend);

    boolean existsByUserUserIdAndFriendUserId(long userId, long friendId);

    UserFriend findByUserUserIdAndFriendUserId(long userId, long friendId);

    void deleteByUserUserId(long userId);

    void deleteByFriendUserId(long userId);
}

