package ra.coursemanagement.business;

import ra.coursemanagement.model.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAll();
    String registerNewStudent(Student student);
    String updateStudentInfo(Student student);
    boolean removeStudent(int id);
    Student getById(int id);
    Student login(String email, String password);
    List<Student> search(String keyword);
}
