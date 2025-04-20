INSERT INTO roles (name)
VALUES
    ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (login, password, email, first_name, surname, middle_name)
VALUES
    ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com', 'Юзер', 'Юзеров', 'Юзерович'),
    ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com', 'Админ', 'Админов', 'Админович');

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO groups (name)
VALUES
    ('test_group');