package ra.coursemanagement.business.impl;

import org.mindrot.jbcrypt.BCrypt;
import ra.coursemanagement.business.StudentService;
import ra.coursemanagement.dao.StudentDAO;
import ra.coursemanagement.dao.impl.StudentDAOImpl;
import ra.coursemanagement.model.Student;

import java.time.LocalDate;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public List<Student> getAll() {
        return studentDAO.getAll();
    }

    @Override
    public String registerNewStudent(Student student) {
        // Nghiệp vụ kiểm tra tính đúng đắn dữ liệu Học viên (Validate)
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Họ tên học viên không được trống!";
        }
        if (student.getDob() == null || student.getDob().isAfter(LocalDate.now())) {
            return "Ngày sinh không hợp lệ!";
        }
        if (student.getEmail() == null || !student.getEmail().contains("@")) {
            return "Định dạng Email học viên không chính xác!";
        }
        if (studentDAO.getByEmail(student.getEmail()) != null) {
            return "Email này đã được đăng ký bởi một học viên khác!";
        }
        if (student.getPhone() == null || student.getPhone().trim().length() < 9) {
            return "Số điện thoại không hợp lệ!";
        }

        // Tiến hành mã hóa bảo mật mật khẩu tài khoản học viên trước khi lưu DB
        String hashedPassword = BCrypt.hashpw(student.getPassword(), BCrypt.gensalt());
        student.setPassword(hashedPassword);

        return studentDAO.add(student) ? "Thành công" : "Đăng ký học viên thất bại.";
    }

    @Override
    public String updateStudentInfo(Student student) {
        Student existing = studentDAO.getById(student.getId());
        if (existing == null) {
            return "Mã định danh học viên không tồn tại!";
        }
        // Kiểm tra email trùng với người khác khi sửa thông tin
        Student checkEmail = studentDAO.getByEmail(student.getEmail());
        if (checkEmail != null && checkEmail.getId() != student.getId()) {
            return "Email chỉnh sửa trùng với học viên khác trên hệ thống!";
        }

        return studentDAO.update(student) ? "Thành công" : "Cập nhật dữ liệu học viên thất bại.";
    }

    @Override
    public boolean removeStudent(int id) {
        return studentDAO.delete(id);
    }

    @Override
    public Student getById(int id) {
        return studentDAO.getById(id);
    }

    @Override
    public Student login(String email, String password) {
        if (email == null || password == null) return null;
        Student student = studentDAO.getByEmail(email.trim());
        if (student != null && BCrypt.checkpw(password, student.getPassword())) {
            return student;
        }
        return null;
    }

    @Override
    public List<Student> search(String keyword) {
        return studentDAO.searchByNameOrEmail(keyword.trim());
    }
}
