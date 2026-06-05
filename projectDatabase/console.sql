CREATE DATABASE IF NOT EXISTS course_management_db;
USE course_management_db;

CREATE TABLE admin (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE student (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         dob DATE NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         sex BIT NOT NULL,
                         phone VARCHAR(20) NOT NULL,
                         password VARCHAR(255) NOT NULL
);

CREATE TABLE course (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        duration INT NOT NULL,
                        instructor VARCHAR(100) NOT NULL,
                        create_at DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE enrollment (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            status ENUM('WAITING', 'DENIED', 'CANCEL', 'CONFIRMED') DEFAULT 'WAITING',
                            FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
                            FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- Chèn tài khoản Admin mẫu (Mật khẩu mã hóa của '123456')
INSERT INTO admin (username, password) VALUES
    ('admin', '$2a$10$wR6vEwMOf5.I9aAOmvP6fOQJ8D8Wk6A9mB.XgUq6Pka8A2N1Sbhv.');