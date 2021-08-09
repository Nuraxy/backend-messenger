package com.xarun.backendmessenger.webSocket;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(
            MessageRepository messageRepository
    ) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage (Message messageToSave) {
        messageRepository.save(messageToSave);
    }

    public List<Message> getAllMissedMessages() {
        return messageRepository.findAllByMessageMissed(true);
    }

    public List<Message> getAllMessagesByIndex(long receiverId, String chatId) {
        return messageRepository.findAllByReceiverIdAndChatId(receiverId, chatId);
    }
}
