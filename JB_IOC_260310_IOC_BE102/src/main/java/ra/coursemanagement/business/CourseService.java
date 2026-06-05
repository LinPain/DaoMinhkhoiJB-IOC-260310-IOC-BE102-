package ra.coursemanagement.business;

import ra.coursemanagement.model.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAll();
    String addCourse(Course course);
    String updateCourse(Course course);
    boolean deleteCourse(int id);
    Course getById(int id);
    List<Course> searchByName(String name);
    List<Course> getSortedCourses(String sortBy, String direction);
}
