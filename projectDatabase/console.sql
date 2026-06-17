CREATE DATABASE course_management;
USE course_management;

DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS admin;

CREATE TABLE admin (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL
);



CREATE TABLE student (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         dob DATE NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         sex BIT NOT NULL,
                         phone VARCHAR(20),
                         password VARCHAR(255) NOT NULL,
                         created_at DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE course (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        duration INT NOT NULL,
                        instructor VARCHAR(100) NOT NULL,
                        created_at DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE enrollment (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            status ENUM(
                                'WAITING',
                                'CONFIRM',
                                'DENIED',
                                'CANCEL'
                                ) DEFAULT 'WAITING',

                            FOREIGN KEY(student_id) REFERENCES student(id),
                            FOREIGN KEY(course_id) REFERENCES course(id),

                            UNIQUE(student_id, course_id)
);
INSERT INTO admin (username, password)
VALUES ('admin', '1234')
ON DUPLICATE KEY UPDATE password = VALUES(password);
INSERT INTO student (name, dob, email, sex, phone, password, created_at) VALUES
                                                                             ('John Doe', '2002-05-14', 'john.doe@email.com', b'1', '555-0101', '$2y$10$pass1', '2026-01-10'),
                                                                             ('Jane Smith', '2003-08-22', 'jane.smith@email.com', b'0', '555-0102', '$2y$10$pass2', '2026-01-11'),
                                                                             ('Michael Brown', '2001-11-05', 'michael.b@email.com', b'1', '555-0103', '$2y$10$pass3', '2026-01-12'),
                                                                             ('Emily Davis', '2004-02-19', 'emily.d@email.com', b'0', '555-0104', '$2y$10$pass4', '2026-01-12'),
                                                                             ('William Jones', '2002-07-31', 'will.jones@email.com', b'1', '555-0105', '$2y$10$pass5', '2026-01-15'),
                                                                             ('Olivia Wilson', '2003-04-12', 'olivia.w@email.com', b'0', '555-0106', '$2y$10$pass6', '2026-01-16'),
                                                                             ('James Taylor', '2002-12-25', 'james.t@email.com', b'1', '555-0107', '$2y$10$pass7', '2026-01-18'),
                                                                             ('Sophia Martinez', '2004-09-09', 'sophia.m@email.com', b'0', '555-0108', '$2y$10$pass8', '2026-01-20'),
                                                                             ('Alexander Anderson', '2001-03-14', 'alex.a@email.com', b'1', '555-0109', '$2y$10$pass9', '2026-01-22'),
                                                                             ('Isabella Thomas', '2003-06-20', 'isabella.t@email.com', b'0', '555-0110', '$2y$10$pass10', '2026-01-25');

INSERT INTO course (name, duration, instructor, created_at) VALUES
                                                                ('Introduction to MySQL', 40, 'Dr. Alan Turing', '2026-01-01'),
                                                                ('Web Development Basics', 60, 'Prof. Tim Berners-Lee', '2026-01-01'),
                                                                ('Advanced Java Programming', 45, 'James Gosling', '2026-01-02'),
                                                                ('Python for Data Science', 50, 'Guido van Rossum', '2026-01-03'),
                                                                ('Cloud Computing Fundamentals', 30, 'Dr. Werner Vogels', '2026-01-04'),
                                                                ('Data Structures & Algorithms', 90, 'Donald Knuth', '2026-01-05'),
                                                                ('Machine Learning Concepts', 75, 'Andrew Ng', '2026-01-06'),
                                                                ('Cybersecurity Essentials', 35, 'Kevin Mitnick', '2026-01-07'),
                                                                ('UI/UX Design Principles', 25, 'Don Norman', '2026-01-08'),
                                                                ('Agile Project Management', 20, 'Jeff Sutherland', '2026-01-09');
INSERT INTO enrollment (student_id, course_id, status) VALUES
                                                           (1, 1, 'CONFIRM'),
                                                           (1, 2, 'WAITING'),
                                                           (2, 2, 'CONFIRM'),
                                                           (3, 4, 'DENIED'),
                                                           (4, 3, 'CONFIRM'),
                                                           (5, 6, 'CANCEL'),
                                                           (6, 7, 'WAITING'),
                                                           (7, 5, 'CONFIRM'),
                                                           (8, 10, 'CONFIRM'),
                                                           (9, 9, 'WAITING');
SELECT id, name FROM course;
SELECT id, name FROM student;