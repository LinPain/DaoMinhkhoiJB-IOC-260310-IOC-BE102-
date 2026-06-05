package ra.coursemanagement.presentation;

import ra.coursemanagement.business.CourseService;
import ra.coursemanagement.business.EnrollmentService;
import ra.coursemanagement.business.impl.CourseServiceImpl;
import ra.coursemanagement.business.impl.EnrollmentServiceImpl;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentView {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();

    public void displayStudentMenu(Student studentSession) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("      MENU CHÍNH - HỌC VIÊN      ");
            System.out.println("=================================");
            System.out.println("Tài khoản: " + studentSession.getName() + " (" + studentSession.getEmail() + ")");
            System.out.println("1. Xem danh sách khóa học đang mở tại trung tâm");
            System.out.println("2. Đăng ký tham gia khóa học mới");
            System.out.println("3. Xem lịch sử gửi yêu cầu đăng ký khóa học");
            System.out.println("0. Đăng xuất");
            System.out.print("Mời bạn chọn chức năng: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": renderAvailableCourses(); break;
                case "2": registerCourseView(studentSession.getId()); break;
                case "3": viewHistoryView(studentSession.getId()); break;
                case "0":
                    System.out.println("Đã đăng xuất tài khoản học viên " + studentSession.getName() + "!");
                    return;
                default:
                    System.out.println("Lựa chọn sai, vui lòng chọn lại chức năng hiển thị trên menu!");
            }
        }
    }

    private void renderAvailableCourses() {
        System.out.println("\n--- DANH SÁCH CÁC KHÓA HỌC ĐANG MỞ ---");
        List<Course> list = courseService.getAll();
        if (list.isEmpty()) {
            System.out.println("Hiện tại trung tâm chưa mở khóa học nào!");
            return;
        }
        System.out.printf("+------+--------------------------------+----------+----------------------+\n");
        System.out.printf("| %-4s | %-30s | %-8s | %-20s |\n", "ID", "Tên Khóa Học", "Số Giờ", "Giảng Viên");
        System.out.printf("+------+--------------------------------+----------+----------------------+\n");
        for (Course c : list) {
            System.out.printf("| %-4d | %-30s | %-8d | %-20s |\n",
                    c.getId(), c.getName(), c.getDuration(), c.getInstructor());
        }
        System.out.printf("+------+--------------------------------+----------+----------------------+\n");
    }

    private void registerCourseView(int studentId) {
        System.out.println("\n--- ĐĂNG KÝ KHÓA HỌC MỚI ---");
        System.out.print("Nhập mã số ID của khóa học bạn muốn học: ");
        int courseId;
        try {
            courseId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Mã khóa học bắt buộc phải nhập chữ số!");
            return;
        }

        String response = enrollmentService.studentRegisterCourse(studentId, courseId);
        if ("Thành công".equalsIgnoreCase(response)) {
            System.out.println("Gửi yêu cầu đăng ký học thành công! Vui lòng chờ Admin phê duyệt.");
        } else {
            System.out.println("Thao tác thất bại: " + response);
        }
    }

    private void viewHistoryView(int studentId) {
        System.out.println("\n--- LỊCH SỬ GỬI ĐƠN VÀ THAM GIA KHÓA HỌC ---");
        List<Map<String, Object>> histories = enrollmentService.getHistoryByStudent(studentId);

        if (histories.isEmpty()) {
            System.out.println("Bạn chưa gửi bất kỳ yêu cầu đăng ký khóa học nào.");
            return;
        }

        System.out.printf("+--------+--------------------------------+---------------------+------------+\n");
        System.out.printf("| %-6s | %-30s | %-19s | %-10s |\n", "Mã đơn", "Tên Môn Học", "Thời Gian Gửi Đơn", "Trạng Thái");
        System.out.printf("+--------+--------------------------------+---------------------+------------+\n");
        for (Map<String, Object> h : histories) {
            System.out.printf("| %-6s | %-30s | %-19s | [%-8s] |\n",
                    h.get("id"), h.get("course_name"), h.get("registered_at"), h.get("status"));
        }
        System.out.printf("+--------+--------------------------------+---------------------+------------+\n");
        System.out.print("\nNhấn Enter để quay lại...");
        scanner.nextLine();
    }
}
