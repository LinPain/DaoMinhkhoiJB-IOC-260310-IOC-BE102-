package ra.coursemanagement.presentation;

import ra.coursemanagement.business.CourseService;
import ra.coursemanagement.business.StudentService;
import ra.coursemanagement.business.EnrollmentService;
import ra.coursemanagement.business.impl.CourseServiceImpl;
import ra.coursemanagement.business.impl.StudentServiceImpl;
import ra.coursemanagement.business.impl.EnrollmentServiceImpl;
import ra.coursemanagement.model.Course;
import ra.coursemanagement.model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminView {
    private final Scanner scanner = new Scanner(System.in);
    private final CourseService courseService = new CourseServiceImpl();
    private final StudentService studentService = new StudentServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();

    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("       MENU CHÍNH - ADMIN        ");
            System.out.println("=================================");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký khóa học");
            System.out.println("4. Thống kê báo cáo");
            System.out.println("0. Đăng xuất");
            System.out.print("Mời bạn chọn chức năng: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": menuCourseManagement(); break;
                case "2": menuStudentManagement(); break;
                case "3": menuEnrollmentManagement(); break;
                case "4": menuStatisticManagement(); break;
                case "0":
                    System.out.println("Đã đăng xuất tài khoản Admin!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
            }
        }
    }

    private void menuCourseManagement() {
        while (true) {
            System.out.println("\n--- MENU QUẢN LÝ KHÓA HỌC ---");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học");
            System.out.println("5. Tìm kiếm khóa học theo tên");
            System.out.println("6. Sắp xếp khóa học");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Mời chọn: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": renderCourseTable(courseService.getAll()); break;
                case "2": addCourseView(); break;
                case "3": editCourseView(); break;
                case "4": deleteCourseView(); break;
                case "5": searchCourseView(); break;
                case "6": sortCourseView(); break;
                case "0": return;
                default: System.out.println("Lựa chọn sai!");
            }
        }
    }

    private void renderCourseTable(List<Course> list) {
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu khóa học nào!");
            return;
        }
        System.out.printf("+------+--------------------------------+----------+----------------------+------------+\n");
        System.out.printf("| %-4s | %-30s | %-8s | %-20s | %-10s |\n", "ID", "Tên Khóa Học", "Số Giờ", "Giảng Viên", "Ngày Tạo");
        System.out.printf("+------+--------------------------------+----------+----------------------+------------+\n");
        for (Course c : list) {
            System.out.printf("| %-4d | %-30s | %-8d | %-20s | %-10s |\n",
                    c.getId(), c.getName(), c.getDuration(), c.getInstructor(), c.getCreateAt().toString());
        }
        System.out.printf("+------+--------------------------------+----------+----------------------+------------+\n");
    }

    private void addCourseView() {
        System.out.println("\n--- THÊM MỚI KHÓA HỌC ---");
        System.out.print("Nhập tên khóa học: "); String name = scanner.nextLine();
        System.out.print("Nhập thời lượng (giờ): ");
        int duration = 0;
        try { duration = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { /* Tầng business sẽ validate */ }
        System.out.print("Nhập giảng viên phụ trách: "); String ins = scanner.nextLine();

        Course course = new Course(0, name, duration, ins, null);
        String msg = courseService.addCourse(course);
        System.out.println("Kết quả: " + msg);
    }

    private void editCourseView() {
        System.out.println("\n--- CHỈNH SỬA KHÓA HỌC ---");
        System.out.print("Nhập ID khóa học cần sửa: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { System.out.println("ID phải là số!"); return; }
        Course current = courseService.getById(id);
        if (current == null) { System.out.println("Không tìm thấy mã khóa học này!"); return; }

        while (true) {
            System.out.println("\nChọn thuộc tính cần sửa:");
            System.out.println("1. Sửa tên khóa học (Hiện tại: " + current.getName() + ")");
            System.out.println("2. Sửa thời lượng (Hiện tại: " + current.getDuration() + "h)");
            System.out.println("3. Sửa giảng viên (Hiện tại: " + current.getInstructor() + ")");
            System.out.println("0. Lưu và Hoàn thành chỉnh sửa");
            System.out.print("Mời chọn: ");
            String opt = scanner.nextLine().trim();

            if ("1".equals(opt)) {
                System.out.print("Nhập tên mới: "); current.setName(scanner.nextLine());
            } else if ("2".equals(opt)) {
                System.out.print("Nhập thời lượng mới: ");
                try { current.setDuration(Integer.parseInt(scanner.nextLine())); } catch (NumberFormatException e) { System.out.println("Sai định dạng số!"); }
            } else if ("3".equals(opt)) {
                System.out.print("Nhập giảng viên mới: "); current.setInstructor(scanner.nextLine());
            } else if ("0".equals(opt)) {
                String msg = courseService.updateCourse(current);
                System.out.println("Kết quả cập nhật: " + msg);
                break;
            } else {
                System.out.println("Lựa chọn không đúng.");
            }
        }
    }

    private void deleteCourseView() {
        System.out.print("Nhập ID khóa học muốn xóa: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Bạn có chắc chắn muốn xóa khóa học này không? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            boolean status = courseService.deleteCourse(id);
            System.out.println(status ? "Xóa khóa học thành công!" : "Xóa thất bại. Kiểm tra lại ID!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }

    private void searchCourseView() {
        System.out.print("Nhập tên khóa học muốn tìm kiếm: ");
        String keyword = scanner.nextLine();
        renderCourseTable(courseService.searchByName(keyword));
    }

    private void sortCourseView() {
        System.out.println("Chọn cột cần sắp xếp: 1. Sắp xếp theo Tên | 2. Sắp xếp theo Thời lượng");
        String fieldOpt = scanner.nextLine().trim();
        String sortBy = "1".equals(fieldOpt) ? "name" : "duration";

        System.out.println("Chọn thứ tự: 1. Tăng dần (ASC) | 2. Giảm dần (DESC)");
        String dirOpt = scanner.nextLine().trim();
        String direction = "2".equals(dirOpt) ? "DESC" : "ASC";

        renderCourseTable(courseService.getSortedCourses(sortBy, direction));
    }

    private void menuStudentManagement() {
        while (true) {
            System.out.println("\n--- MENU QUẢN LÝ HỌC VIÊN ---");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa học viên");
            System.out.println("4. Xóa học viên");
            System.out.println("5. Tìm kiếm học viên theo Tên hoặc Email");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Mời chọn: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": renderStudentTable(studentService.getAll()); break;
                case "2": addStudentView(); break;
                case "3": editStudentView(); break;
                case "4": deleteStudentView(); break;
                case "5": searchStudentView(); break;
                case "0": return;
                default: System.out.println("Lựa chọn sai!");
            }
        }
    }

    private void renderStudentTable(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("Không có học viên nào!");
            return;
        }
        System.out.printf("+------+----------------------+------------+---------------------------+-----------+--------------+\n");
        System.out.printf("| %-4s | %-20s | %-10s | %-25s | %-9s | %-12s |\n", "ID", "Họ Tên", "Ngày Sinh", "Email", "Giới Tính", "Điện Thoại");
        System.out.printf("+------+----------------------+------------+---------------------------+-----------+--------------+\n");
        for (Student s : list) {
            System.out.printf("| %-4d | %-20s | %-10s | %-25s | %-9s | %-12s |\n",
                    s.getId(), s.getName(), s.getDob().toString(), s.getEmail(), s.isSex() ? "Nam" : "Nữ", s.getPhone());
        }
        System.out.printf("+------+----------------------+------------+---------------------------+-----------+--------------+\n");
    }

    private void addStudentView() {
        System.out.println("\n--- THÊM MỚI HỌC VIÊN ---");
        System.out.print("Nhập họ tên học viên: "); String name = scanner.nextLine();
        System.out.print("Nhập ngày sinh (Định dạng chuẩn YYYY-MM-DD): ");
        LocalDate dob = null;
        try { dob = LocalDate.parse(scanner.nextLine().trim()); } catch (DateTimeParseException e) { /* Validate tầng business */ }
        System.out.print("Nhập Email đăng nhập: "); String email = scanner.nextLine();
        System.out.print("Giới tính (1: Nam / 0: Nữ): "); boolean sex = "1".equals(scanner.nextLine().trim());
        System.out.print("Nhập số điện thoại: "); String phone = scanner.nextLine();
        System.out.print("Nhập mật khẩu tài khoản học viên: "); String password = scanner.nextLine();

        Student student = new Student(0, name, dob, email, sex, phone, password);
        String msg = studentService.registerNewStudent(student);
        System.out.println("Kết quả: " + msg);
    }

    private void editStudentView() {
        System.out.println("\n--- CHỈNH SỬA HỌC VIÊN ---");
        System.out.print("Nhập ID học viên cần sửa: ");
        int id = Integer.parseInt(scanner.nextLine());
        Student current = studentService.getById(id);
        if (current == null) { System.out.println("Học viên không tồn tại!"); return; }

        System.out.print("Nhập họ tên mới (Bỏ trống nếu giữ cũ ["+current.getName()+"]): ");
        String name = scanner.nextLine(); if(!name.trim().isEmpty()) current.setName(name);

        System.out.print("Nhập ngày sinh mới YYYY-MM-DD (Bỏ trống nếu giữ cũ): ");
        String dobStr = scanner.nextLine().trim();
        if(!dobStr.isEmpty()) { try { current.setDob(LocalDate.parse(dobStr)); } catch(Exception e) { System.out.println("Định dạng ngày sai!"); } }

        System.out.print("Nhập Email mới (Bỏ trống nếu giữ cũ): ");
        String email = scanner.nextLine(); if(!email.trim().isEmpty()) current.setEmail(email);

        System.out.print("Nhập SĐT mới (Bỏ trống nếu giữ cũ): ");
        String phone = scanner.nextLine(); if(!phone.trim().isEmpty()) current.setPhone(phone);

        String msg = studentService.updateStudentInfo(current);
        System.out.println("Kết quả: " + msg);
    }

    private void deleteStudentView() {
        System.out.print("Nhập ID học viên muốn xóa khỏi hệ thống: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Hành động này sẽ xóa toàn bộ lịch sử học tập liên quan! Có tiếp tục? (Y/N): ");
        if(scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            boolean status = studentService.removeStudent(id);
            System.out.println(status ? "Xóa học viên thành công!" : "Xóa thất bại!");
        }
    }

    private void searchStudentView() {
        System.out.print("Nhập tên hoặc email học viên cần tìm: ");
        String kw = scanner.nextLine();
        renderStudentTable(studentService.search(kw));
    }

    private void menuEnrollmentManagement() {
        while (true) {
            System.out.println("\n--- DANH SÁCH ĐĂNG KÝ CHỜ PHÊ DUYỆT ---");
            List<Map<String, Object>> enrollments = enrollmentService.getPendingAndAllEnrollments();
            if(enrollments.isEmpty()) {
                System.out.println("Không tồn tại bản ghi đăng ký nào.");
            } else {
                System.out.printf("+--------+----------------------+--------------------------------+---------------------+------------+\n");
                System.out.printf("| %-6s | %-20s | %-30s | %-19s | %-10s |\n", "Mã ĐK", "Tên Học Viên", "Khóa Học Đăng Ký", "Thời Gian", "Trạng Thái");
                System.out.printf("+--------+----------------------+--------------------------------+---------------------+------------+\n");
                for(Map<String, Object> map : enrollments) {
                    System.out.printf("| %-6s | %-20s | %-30s | %-19s | %-10s |\n",
                            map.get("enrollment_id"), map.get("student_name"), map.get("course_name"), map.get("registered_at"), map.get("status"));
                }
                System.out.printf("+--------+----------------------+--------------------------------+---------------------+------------+\n");
            }

            System.out.println("\nCác tác vụ hành động:");
            System.out.println("1. Phê duyệt/Thay đổi trạng thái đơn đăng ký học");
            System.out.println("0. Quay lại");
            System.out.print("Mời chọn: ");
            String act = scanner.nextLine().trim();

            if("1".equals(act)) {
                System.out.print("Nhập Mã Đăng ký (Mã ĐK) cần xử lý: ");
                int eId = Integer.parseInt(scanner.nextLine());
                System.out.println("Thiết lập trạng thái mới: 1. CONFIRMED (Phê duyệt) | 2. DENIED (Từ chối) | 3. CANCEL (Hủy bỏ)");
                System.out.print("Chọn: ");
                String statusOpt = scanner.nextLine().trim();
                String targetStatus = "WAITING";
                if("1".equals(statusOpt)) targetStatus = "CONFIRMED";
                if("2".equals(statusOpt)) targetStatus = "DENIED";
                if("3".equals(statusOpt)) targetStatus = "CANCEL";

                boolean success = enrollmentService.adminReviewEnrollment(eId, targetStatus);
                System.out.println(success ? "Cập nhật trạng thái duyệt đơn thành công!" : "Xử lý thất bại! Sai mã hoặc sai trạng thái.");
            } else {
                return;
            }
        }
    }

    private void menuStatisticManagement() {
        System.out.println("\n--- BÁO CÁO THỐNG KÊ SỐ LƯỢNG HỌC VIÊN ---");
        List<Map<String, Object>> reports = enrollmentService.getCourseStatisticsReport();
        System.out.printf("+-----------+--------------------------------+--------------------------+\n");
        System.out.printf("| %-9s | %-30s | %-24s |\n", "Mã Khóa", "Tên Khóa Học Đang Mở", "Số Học Viên Thực Tế (Đã Duyệt)");
        System.out.printf("+-----------+--------------------------------+--------------------------+\n");
        for (Map<String, Object> r : reports) {
            System.out.printf("| %-9s | %-30s | %-24s |\n",
                    r.get("course_id"), r.get("course_name"), r.get("total_students"));
        }
        System.out.printf("+-----------+--------------------------------+--------------------------+\n");
        System.out.print("\nNhấn Enter để quay lại...");
        scanner.nextLine();
    }
}
