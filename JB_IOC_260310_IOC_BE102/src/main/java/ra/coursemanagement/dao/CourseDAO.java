package ra.coursemanagement.dao;

import ra.coursemanagement.model.Course;
import java.util.List;

public interface CourseDAO {
    List<Course> getAll();
    boolean add(Course course);
    boolean update(Course course);
    boolean delete(int id);
    Course getById(int id);
    List<Course> searchByName(String name);
    List<Course> getAllSorted(String sortBy, String direction);
}
