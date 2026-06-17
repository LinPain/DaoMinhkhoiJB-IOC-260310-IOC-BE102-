CREATE DATABASE IF NOT EXISTS course_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE course_management;

CREATE TABLE IF NOT EXISTS admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BOOLEAN NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS enrollment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);

INSERT INTO admin (username, password)
VALUES ('admin', '2134')
ON DUPLICATE KEY UPDATE password = VALUES(password);

INSERT INTO student (name, dob, email, sex, phone, password)
SELECT 'Nguyen Van A', '2000-01-15', 'student@example.com', true, '0900000000', 'student123'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE email = 'student@example.com');

INSERT INTO course (name, duration, instructor)
SELECT 'Java Core', 40, 'John Smith'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = 'Java Core');

INSERT INTO course (name, duration, instructor)
SELECT 'Java JDBC', 24, 'Jane Doe'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = 'Java JDBC');
