package com.xarun.backendmessenger.webSocket;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(
            MessageRepository messageRepository
    ) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage (Message messageToSave) {
        if (messageToSave.getReadFlag() == null){
         messageToSave.setReadFlag(false);
        }
        return messageRepository.save(messageToSave);
    }
}
