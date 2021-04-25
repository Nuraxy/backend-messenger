create table user(
    id bigint auto_increment,
    user_role_id bigint DEFAULT 2,
    name VARCHAR(255) NOT NUll,
    password VARCHAR(255) NOT NUll,
    email VARCHAR(255) NOT NUll,
    salt TINYBLOB DEFAULT NULL,
    confirmed boolean DEFAULT FALSE,
    registration_date  DATETIME NOT NUll,
    register_key varchar(255) DEFAULT null,
    login_mode boolean DEFAULT false,
    session_id VARCHAR(255) NUll,
    public_key_e bigint NULL,
    public_key_n bigint NULL,

    constraint pk_user primary key (id),
    constraint unique_name unique (name),
    constraint unique_email unique (email)
);

create table user_role(
    id bigint auto_increment,
    role_name VARCHAR(255) NOT NUll,

    constraint pk_userrole primary key (id),
    constraint unique_userrole_rolename unique (role_name)
);

create table user_friend(
    id bigint auto_increment,
    user_id bigint null,
    friend_id bigint null,
    confirmed boolean DEFAULT false,

    constraint pk_user_user primary key (id),
    constraint fk_user_friend foreign key (user_id) references user(id),
    constraint fk_friend_user foreign key (friend_id) references user(id),
    constraint uc_user_relationship unique (user_id,friend_id)
);

create table token(
    id bigint auto_increment,
    user_id bigint NOT NUll,
    token_value VARCHAR(255) NULL,
    last_request DATETIME NOT NUll,

    constraint pk_token primary key (id),
    constraint fk_token_user foreign key (user_id) references user(id)
);
