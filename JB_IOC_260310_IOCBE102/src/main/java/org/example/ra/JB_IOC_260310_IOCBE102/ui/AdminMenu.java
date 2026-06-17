package org.example.ra.JB_IOC_260310_IOCBE102.ui;

import org.example.ra.JB_IOC_260310_IOCBE102.model.Course;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Student;
import org.example.ra.JB_IOC_260310_IOCBE102.service.AppException;
import org.example.ra.JB_IOC_260310_IOCBE102.service.CourseService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.EnrollmentService;
import org.example.ra.JB_IOC_260310_IOCBE102.service.StudentService;

public class AdminMenu {
    private final Console console;
    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public AdminMenu(Console console, CourseService courseService, StudentService studentService,
                     EnrollmentService enrollmentService) {
        this.console = console;
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    public void start() {
        while (true) {
            console.title("ADMIN MENU");
            console.line("1. Manage courses");
            console.line("2. Manage students");
            console.line("3. Manage course registrations");
            console.line("4. Statistics");
            console.line("0. Logout");
            int choice = console.readInt("Choose");
            try {
                switch (choice) {
                    case 1 -> courses();
                    case 2 -> students();
                    case 3 -> enrollments();
                    case 4 -> statistics();
                    case 0 -> {
                        return;
                    }
                    default -> console.error("Invalid choice.");
                }
            } catch (AppException exception) {
                console.error(exception.getMessage());
            }
        }
    }

    private void courses() {
        while (true) {
            console.title("COURSE MANAGEMENT");
            console.line("1. List courses");
            console.line("2. Add course");
            console.line("3. Update course");
            console.line("4. Delete course");
            console.line("5. Search by name");
            console.line("6. Sort by name");
            console.line("7. Sort by duration");
            console.line("0. Back");
            int choice = console.readInt("Choose");
            switch (choice) {
                case 1 -> TablePrinter.courses(console, courseService.findAll());
                case 2 -> addCourse();
                case 3 -> updateCourse();
                case 4 -> deleteCourse();
                case 5 -> TablePrinter.courses(console, courseService.searchByName(console.readLine("Keyword")));
                case 6 -> TablePrinter.courses(console, courseService.sortByName());
                case 7 -> TablePrinter.courses(console, courseService.sortByDuration());
                case 0 -> {
                    return;
                }
                default -> console.error("Invalid choice.");
            }
        }
    }

    private void addCourse() {
        Course course = new Course();
        course.setName(console.readLine("Name"));
        course.setDuration(console.readInt("Duration (hours)"));
        course.setInstructor(console.readLine("Instructor"));
        courseService.create(course);
        console.success("Course created.");
    }

    private void updateCourse() {
        int id = console.readInt("Course ID");
        Course course = courseService.getRequired(id);
        course.setName(console.readOptional("Name", course.getName()));
        course.setDuration(console.readOptionalInt("Duration (hours)", course.getDuration()));
        course.setInstructor(console.readOptional("Instructor", course.getInstructor()));
        courseService.update(course);
        console.success("Course updated.");
    }

    private void deleteCourse() {
        int id = console.readInt("Course ID");
        if (console.confirm("Delete this course")) {
            courseService.delete(id);
            console.success("Course deleted.");
        }
    }

    private void students() {
        while (true) {
            console.title("STUDENT MANAGEMENT");
            console.line("1. List students");
            console.line("2. Add student");
            console.line("3. Update student");
            console.line("4. Delete student");
            console.line("5. Search by name/email");
            console.line("0. Back");
            int choice = console.readInt("Choose");
            switch (choice) {
                case 1 -> TablePrinter.students(console, studentService.findAll());
                case 2 -> addStudent();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> TablePrinter.students(console, studentService.search(console.readLine("Keyword")));
                case 0 -> {
                    return;
                }
                default -> console.error("Invalid choice.");
            }
        }
    }

    private void addStudent() {
        Student student = new Student();
        student.setName(console.readLine("Name"));
        student.setDob(console.readDate("Date of birth (YYYY-MM-DD)"));
        student.setEmail(console.readLine("Email"));
        student.setMale(console.readBoolean("Male (Y/N)"));
        student.setPhone(console.readLine("Phone"));
        student.setPassword(console.readLine("Password"));
        studentService.create(student);
        console.success("Student created.");
    }

    private void updateStudent() {
        int id = console.readInt("Student ID");
        Student student = studentService.getRequired(id);
        student.setName(console.readOptional("Name", student.getName()));
        student.setEmail(console.readOptional("Email", student.getEmail()));
        student.setPhone(console.readOptional("Phone", student.getPhone()));
        student.setPassword(console.readOptional("Password", student.getPassword()));
        studentService.update(student);
        console.success("Student updated.");
    }

    private void deleteStudent() {
        int id = console.readInt("Student ID");
        if (console.confirm("Delete this student")) {
            studentService.delete(id);
            console.success("Student deleted.");
        }
    }

    private void enrollments() {
        while (true) {
            console.title("REGISTRATION MANAGEMENT");
            console.line("1. List all registrations");
            console.line("2. Register student to course");
            console.line("3. Delete registration");
            console.line("4. List students by course");
            console.line("0. Back");
            int choice = console.readInt("Choose");
            switch (choice) {
                case 1 -> TablePrinter.enrollments(console, enrollmentService.findAll());
                case 2 -> registerStudent();
                case 3 -> deleteEnrollment();
                case 4 -> TablePrinter.enrollments(console, enrollmentService.findByCourse(console.readInt("Course ID")));
                case 0 -> {
                    return;
                }
                default -> console.error("Invalid choice.");
            }
        }
    }

    private void registerStudent() {
        TablePrinter.students(console, studentService.findAll());
        int studentId = console.readInt("Student ID");
        TablePrinter.courses(console, courseService.findOpenCourses());
        int courseId = console.readInt("Course ID");
        enrollmentService.register(studentId, courseId);
        console.success("Registration created.");
    }

    private void deleteEnrollment() {
        TablePrinter.enrollments(console, enrollmentService.findAll());
        int id = console.readInt("Registration ID");
        if (console.confirm("Delete this registration")) {
            enrollmentService.delete(id);
            console.success("Registration deleted.");
        }
    }

    private void statistics() {
        console.title("STATISTICS");
        TablePrinter.statistics(console, enrollmentService.countStudentsByCourse());
    }
}
