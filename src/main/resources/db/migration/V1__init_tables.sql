-- Таблицы для системы ролей
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

-- Группы создаются первыми, так как Users ссылается на Groups
CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Основная таблица пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    group_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE SET NULL
);

-- Связь пользователей с ролями
CREATE TABLE users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Файлы системы
CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Материалы для заданий
CREATE TABLE materials (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_id INT,
    teacher_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE SET NULL
);

-- Учебные задания
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id INT NOT NULL,
    material_id INT,
    deadline TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES materials(id) ON DELETE SET NULL
);

-- Связь заданий с группами
CREATE TABLE task_groups (
    id SERIAL PRIMARY KEY,
    task_id INT NOT NULL,
    group_id INT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_task_group UNIQUE (task_id, group_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
);

-- Ответы на задания
CREATE TABLE task_submissions (
    id SERIAL PRIMARY KEY,
    task_id INT NOT NULL,
    student_id INT NOT NULL,
    text TEXT,
    file_id INT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE SET NULL
);

-- Оценки
CREATE TABLE grades (
    id SERIAL PRIMARY KEY,
    submission_id INT NOT NULL,
    grade INT NOT NULL CHECK (grade >= 1 AND grade <= 5),
    teacher_id INT NOT NULL,
    graded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (submission_id) REFERENCES task_submissions(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Консультации
CREATE TABLE consultations (
    id SERIAL PRIMARY KEY,
    teacher_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Связь консультаций с группами
CREATE TABLE consultation_Groups (
    id SERIAL PRIMARY KEY,
    consultation_id INT NOT NULL,
    group_id INT NOT NULL,
    CONSTRAINT uq_consultation_group UNIQUE (consultation_id, group_id),
    FOREIGN KEY (consultation_id) REFERENCES consultations(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE
);

-- Новая таблица дисциплин
CREATE TABLE disciplines (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teacher_disciplines (
    teacher_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    discipline_id INT NOT NULL REFERENCES disciplines(id) ON DELETE CASCADE,
    PRIMARY KEY (teacher_id, discipline_id)
);
CREATE TABLE discipline_group (
    discipline_id INT NOT NULL REFERENCES disciplines(id),
    group_id INT NOT NULL REFERENCES groups(id),
    PRIMARY KEY (discipline_id, group_id)
);

-- Добавляем дисциплину к заданиям
ALTER TABLE Tasks ADD COLUMN discipline_id INT NOT NULL REFERENCES disciplines(id) ON DELETE CASCADE;

-- Добавляем дисциплину к консультациям
ALTER TABLE Consultations ADD COLUMN discipline_id INT NOT NULL REFERENCES disciplines(id) ON DELETE CASCADE;