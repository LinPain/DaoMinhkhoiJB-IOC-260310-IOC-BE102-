package ra.coursemanagement;

import ra.coursemanagement.business.AdminService;
import ra.coursemanagement.business.StudentService;
import ra.coursemanagement.business.impl.AdminServiceImpl;
import ra.coursemanagement.business.impl.StudentServiceImpl;
import ra.coursemanagement.model.Student;
import ra.coursemanagement.presentation.AdminView;
import ra.coursemanagement.presentation.StudentView;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminService adminService = new AdminServiceImpl();
    private static final StudentService studentService = new StudentServiceImpl();
    private static final AdminView adminView = new AdminView();
    private static final StudentView studentView = new StudentView();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=================================================");
            System.out.println("    CHƯƠNG TRÌNH QUẢN LÝ KHÓA HỌC & HỌC VIÊN     ");
            System.out.println("=================================================");
            System.out.println("1. Đăng nhập quyền Quản trị viên (Admin)");
            System.out.println("2. Đăng nhập quyền Học viên (Student)");
            System.out.println("0. Thoát chương trình");
            System.out.print("Mời bạn chọn vai trò hệ thống: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAdminLogin();
                    break;
                case "2":
                    handleStudentLogin();
                    break;
                case "0":
                    System.out.println("\nCảm ơn bạn đã sử dụng hệ thống quản lý đào tạo. Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn (1, 2 hoặc 0).");
            }
        }
    }

    /**
     * Xử lý luồng nghiệp vụ đăng nhập của Quản trị viên
     */
    private static void handleAdminLogin() {
        System.out.println("\n--- ĐĂNG NHẬP HỆ THỐNG - QUẢN TRỊ VIÊN ---");
        System.out.print("Nhập tên tài khoản (Username): ");
        String username = scanner.nextLine().trim();
        System.out.print("Nhập mật khẩu (Password): ");
        String password = scanner.nextLine();

        // Kiểm tra tính đúng đắn qua tầng nghiệp vụ (Business Service)
        boolean loginSuccess = adminService.login(username, password);

        if (loginSuccess) {
            System.out.println("\n>>> Đăng nhập quyền Admin thành công!");
            // Điều hướng sang phân hệ Menu dành riêng cho Admin tại tầng Presentation
            adminView.displayMainMenu();
        } else {
            System.out.println("❌ Đăng nhập thất bại! Sai tên tài khoản hoặc mật khẩu Quản trị viên.");
        }
    }

    private static void handleStudentLogin() {
        System.out.println("\n--- ĐĂNG NHẬP HỆ THỐNG - HỌC VIÊN ---");
        System.out.print("Nhập địa chỉ Email học viên: ");
        String email = scanner.nextLine().trim();
        System.out.print("Nhập mật khẩu bảo mật: ");
        String password = scanner.nextLine();

        Student loggedInStudent = studentService.login(email, password);

        if (loggedInStudent != null) {
            System.out.println("\n>>> Đăng nhập tài khoản Học viên thành công!");
            studentView.displayStudentMenu(loggedInStudent);
        } else {
            System.out.println("❌ Đăng nhập thất bại! Email hoặc mật khẩu học viên chưa chính xác.");
        }
    }
}
