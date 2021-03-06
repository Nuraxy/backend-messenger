create table message(
     message_id bigint auto_increment,
     message_type VARCHAR(255) NOT NUll,
     sender_id bigint NOT NULL,
     receiver_id bigint NOT NULL,
     chat_id VARCHAR(255) NOT NUll,
     message VARCHAR(1024) NOT NUll,
     message_missed boolean DEFAULT TRUE,

     constraint pk_message primary key (message_id),
     constraint fk_message_user_sender foreign key (sender_id) references user(user_id),
     constraint fk_message_user_receiver foreign key (receiver_id) references user(user_id),
     index idx_receiver_chat (receiver_id, chat_id)
);
