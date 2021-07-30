INSERT INTO user (user_id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (1, 2, 'Marc', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Marc.Schnell@lineas.de', null, true, '2019-12-16 07:39:11', null, null, null, null);
INSERT INTO user (user_id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (2, 2, 'Marc2', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Marc.Schnell@MarcSch.de', null, true, '2019-12-16 07:39:11', null, null, null, null);
INSERT INTO user (user_id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (3, 2, 'Niclas', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Niclas.Lang@lineas.de', null, true, '2019-12-16 07:39:11', null, null, null, null);
INSERT INTO user (user_id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (4, 2, 'Sven', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Sven.Kunze@lineas.de', null, true, '2019-12-16 07:39:11', null, null, null, null);

INSERT INTO user_role (id, role_name)
    VALUE (1, 'Admin');
INSERT INTO user_role (id, role_name)
    VALUE (2, 'Public');
INSERT INTO user_role (id, role_name)
    VALUE (3, 'Private');

INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (1, 1, 1, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (2, 1, 2, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (3, 1, 3, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (4, 2, 2, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (5, 2, 1, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (6, 2, 3, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (7, 3, 3, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (8, 3, 1, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (9, 3, 2, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (10, 1, 4, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (11, 4, 4, true);
INSERT INTO user_friend (id, user_id, friend_id, confirmed)
    VALUE (12, 4, 1, true);
