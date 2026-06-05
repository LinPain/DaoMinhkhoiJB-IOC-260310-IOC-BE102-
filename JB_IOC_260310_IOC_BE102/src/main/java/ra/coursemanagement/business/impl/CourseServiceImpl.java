package ra.coursemanagement.business.impl;

import ra.coursemanagement.business.CourseService;
import ra.coursemanagement.dao.CourseDAO;
import ra.coursemanagement.dao.impl.CourseDAOImpl;
import ra.coursemanagement.model.Course;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public List<Course> getAll() {
        return courseDAO.getAll();
    }

    @Override
    public String addCourse(Course course) {
        // Validate dữ liệu bắt buộc không để trống theo đặc tả
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            return "Tên khóa học không được phép để trống!";
        }
        if (course.getDuration() <= 0) {
            return "Thời lượng khóa học phải lớn hơn 0 giờ!";
        }
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
            return "Tên giảng viên phụ trách không được trống!";
        }

        return courseDAO.add(course) ? "Thành công" : "Lỗi hệ thống: Thêm khóa học thất bại.";
    }

    @Override
    public String updateCourse(Course course) {
        Course existing = courseDAO.getById(course.getId());
        if (existing == null) {
            return "Mã khóa học không tồn tại trên hệ thống!";
        }
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            return "Tên khóa học cập nhật không được trống!";
        }
        if (course.getDuration() <= 0) {
            return "Thời lượng cập nhật phải lớn hơn 0 giờ!";
        }

        return courseDAO.update(course) ? "Thành công" : "Cập nhật khóa học thất bại.";
    }

    @Override
    public boolean deleteCourse(int id) {
        return courseDAO.delete(id);
    }

    @Override
    public Course getById(int id) {
        return courseDAO.getById(id);
    }

    @Override
    public List<Course> searchByName(String name) {
        return courseDAO.searchByName(name.trim());
    }

    @Override
    public List<Course> getSortedCourses(String sortBy, String direction) {
        // Validate chuẩn hóa tham số đầu vào chống phá câu lệnh SQL
        String validSortBy = "duration".equalsIgnoreCase(sortBy) ? "duration" : "name";
        String validDirection = "DESC".equalsIgnoreCase(direction) ? "DESC" : "ASC";
        return courseDAO.getAllSorted(validSortBy, validDirection);
    }
}
