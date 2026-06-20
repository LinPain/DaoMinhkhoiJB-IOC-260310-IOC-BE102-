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
        try {
            int id = console.readIntOrBack("Course ID");
            Course course = courseService.getRequired(id);
            String name = console.readOptionalOrBack("Name", course.getName());
            int duration = console.readOptionalIntOrBack("Duration (hours)", course.getDuration());
            String instructor = console.readOptionalOrBack("Instructor", course.getInstructor());
            course.setName(name);
            course.setDuration(duration);
            course.setInstructor(instructor);
            courseService.update(course);
            console.success("Course updated.");
        } catch (InputCancelledException exception) {
            console.line("Update cancelled.");
        }
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
        student.setDob(console.readDate("Date of birth"));
        student.setEmail(console.readEmail("Email"));
        student.setMale(console.readBoolean("Gender"));
        student.setPhone(console.readLine("Phone"));
        student.setPassword(console.readLine("Password"));
        studentService.create(student);
        console.success("Student created.");
    }

    private void updateStudent() {
        try {
            int id = console.readIntOrBack("Student ID");
            Student student = studentService.getRequired(id);
            String name = console.readOptionalOrBack("Name", student.getName());
            String email = console.readOptionalEmailOrBack("Email", student.getEmail());
            String phone = console.readOptionalOrBack("Phone", student.getPhone());
            String password = console.readOptionalOrBack("Password", student.getPassword());
            student.setName(name);
            student.setEmail(email);
            student.setPhone(phone);
            student.setPassword(password);
            studentService.update(student);
            console.success("Student updated.");
        } catch (InputCancelledException exception) {
            console.line("Update cancelled.");
        }
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
            console.line("5. Update registration status");
            console.line("0. Back");
            int choice = console.readInt("Choose");
            switch (choice) {
                case 1 -> TablePrinter.enrollments(console, enrollmentService.findAll());
                case 2 -> registerStudent();
                case 3 -> deleteEnrollment();
                case 4 -> TablePrinter.enrollments(console, enrollmentService.findByCourse(console.readInt("Course ID")));
                case 5 -> updateEnrollmentStatus();
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

    private void updateEnrollmentStatus() {
        TablePrinter.enrollments(console, enrollmentService.findAll());
        int id = console.readInt("Registration ID");
        console.line("1. WAITING");
        console.line("2. CONFIRM");
        console.line("3. DENIED");
        console.line("4. CANCEL");
        int choice = console.readInt("Status");
        String status = switch (choice) {
            case 1 -> "WAITING";
            case 2 -> "CONFIRM";
            case 3 -> "DENIED";
            case 4 -> "CANCEL";
            default -> null;
        };
        if (status == null) {
            console.error("Invalid status.");
            return;
        }
        enrollmentService.updateStatus(id, status);
        console.success("Registration status updated.");
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
