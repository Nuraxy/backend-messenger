INSERT INTO user (id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (1, 1, 'Marc', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Marc.Schnell@lineas.de', null, true, '2019-12-16 07:39:11', null, null, null, null);
INSERT INTO user (id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (2, 2, 'Marc2', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Marc.Schnell@MarcSch.de', null, true, '2019-12-16 07:39:11', null, null, null, null);
INSERT INTO user (id, user_role_id, name, password, email, salt, confirmed, registration_date, register_key, login_mode, session_id, public_key)
    VALUE (3, 1, 'Niclas', '2SYsG9uKOKdl8+aLAw3Rta259q25GyYXk/xIRrQOyGU=', 'Niclas.Lang@lineas.de', null, true, '2019-12-16 07:39:11', null, null, null, null);

INSERT INTO user_role (id, role_name)
    VALUE (1, 'Admin');
INSERT INTO user_role (id, role_name)
    VALUE (2, 'Public');
INSERT INTO user_role (id, role_name)
    VALUE (3, 'Private');
