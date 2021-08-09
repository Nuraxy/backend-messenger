package com.xarun.backendmessenger.webSocket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Long>{

    List<Message> findAllByMessageId(long messageId);

    List<Message> findAllByMessageMissed(boolean b);

    List<Message> findAllByReceiverIdAndChatId(long receiverId, String chatId);
}
