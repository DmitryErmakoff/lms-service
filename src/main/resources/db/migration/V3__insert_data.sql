-- Добавляем еще студентов
INSERT INTO users (login, password, email, first_name, surname, middle_name, group_id)
VALUES
    ('student1', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'student1@gmail.com', 'Иван', 'Иванов', 'Иванович', 1),
    ('student2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'student2@gmail.com', 'Петр', 'Петров', 'Петрович', 1),
    ('student3', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'student3@gmail.com', 'Сергей', 'Сергеев', 'Сергеевич', 1),
    ('student4', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'student4@gmail.com', 'Алексей', 'Алексеев', 'Алексеевич', 1);

-- Добавляем роли студентам
INSERT INTO users_roles (user_id, role_id)
VALUES
    (3, 1),
    (4, 1),
    (5, 1),
    (6, 1);

-- Добавляем дисциплины
INSERT INTO disciplines (name, description)
VALUES
    ('Математика', 'Основы математического анализа и линейной алгебры'),
    ('Программирование', 'Изучение языков программирования и алгоритмов'),
    ('Физика', 'Классическая механика и термодинамика');

-- Назначаем преподавателей на дисциплины
INSERT INTO teacher_disciplines (teacher_id, discipline_id)
VALUES
    (2, 1),  -- Админ преподает математику
    (2, 2);  -- Админ преподает программирование

-- Назначаем дисциплины группе
INSERT INTO discipline_group (discipline_id, group_id)
VALUES
    (1, 1),  -- Математика для test_group
    (2, 1);  -- Программирование для test_group