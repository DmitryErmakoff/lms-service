CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE disciplines (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    group_id INT,
    FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE theory_material (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    discipline_id INT,
    files TEXT,
    teacher_id INT,
    FOREIGN KEY (discipline_id) REFERENCES disciplines(id),
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE assignment (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    discipline_id INT,
    files TEXT,
    teacher_id INT,
    FOREIGN KEY (discipline_id) REFERENCES disciplines(id),
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE assignment_theory_material (
    assignment_id INT NOT NULL,
    theory_material_id INT NOT NULL,
    PRIMARY KEY (assignment_id, theory_material_id),
    FOREIGN KEY (assignment_id) REFERENCES assignment(id),
    FOREIGN KEY (theory_material_id) REFERENCES theory_material(id)
);

CREATE TABLE assignment_deadline (
    id SERIAL PRIMARY KEY,
    assignment_id INT NOT NULL,
    group_id INT NOT NULL,
    deadline TIMESTAMP NOT NULL,
    FOREIGN KEY (assignment_id) REFERENCES assignment(id),
    FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE teacher_group (
    teacher_id INT NOT NULL,
    group_id INT NOT NULL,
    PRIMARY KEY (teacher_id, group_id),
    FOREIGN KEY (teacher_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE Grade (
    id SERIAL PRIMARY KEY,
    assignment_id INT,
    student_id INT,
    grade DECIMAL(5, 2),
    comment TEXT,
    FOREIGN KEY (assignment_id) REFERENCES assignment(id),
    FOREIGN KEY (student_id) REFERENCES users(id)
);







