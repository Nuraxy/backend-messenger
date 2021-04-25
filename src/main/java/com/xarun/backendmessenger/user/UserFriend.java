package com.xarun.backendmessenger.user;

import javax.persistence.*;

@Table(name = "user_friend")
@Entity
public class UserFriend {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="friend_id")
    private User friend;

    @Column(name="confirmed")
    public boolean confirmed;

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
