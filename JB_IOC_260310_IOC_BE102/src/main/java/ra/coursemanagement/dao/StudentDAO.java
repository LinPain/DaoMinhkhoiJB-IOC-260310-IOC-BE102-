package ra.coursemanagement.dao;

import ra.coursemanagement.model.Student;
import java.util.List;

public interface StudentDAO {
    List<Student> getAll();
    boolean add(Student student);
    boolean update(Student student);
    boolean delete(int id);
    Student getById(int id);
    Student getByEmail(String email);
    List<Student> searchByNameOrEmail(String keyword);
}
