package com.xarun.backendmessenger.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Transactional
    void deleteAllByUserUserId(long userId);

    Token getFirstByTokenValue (String tokenValue);

    @Modifying
    @Query("DELETE FROM Token WHERE tokenId = :id ")
    void deleteById(@Param("id") long tokenId);
}
