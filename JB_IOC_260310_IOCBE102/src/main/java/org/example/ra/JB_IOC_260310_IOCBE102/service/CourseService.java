package org.example.ra.JB_IOC_260310_IOCBE102.service;

import org.example.ra.JB_IOC_260310_IOCBE102.dao.CourseDao;
import org.example.ra.JB_IOC_260310_IOCBE102.model.Course;

import java.util.List;

public class CourseService {
    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public List<Course> findAll() {
        return courseDao.findAll();
    }

    public List<Course> findOpenCourses() {
        return courseDao.findOpenCourses();
    }

    public List<Course> sortByName() {
        return courseDao.findSorted("name");
    }

    public List<Course> sortByDuration() {
        return courseDao.findSorted("duration");
    }

    public List<Course> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return courseDao.searchByName(keyword.trim());
    }

    public Course create(Course course) {
        validate(course);
        return courseDao.create(course);
    }

    public void update(Course course) {
        validate(course);
        if (!courseDao.update(course)) {
            throw new AppException("Course not found.");
        }
    }

    public void delete(int id) {
        if (!courseDao.delete(id)) {
            throw new AppException("Course not found.");
        }
    }

    public Course getRequired(int id) {
        return courseDao.findById(id).orElseThrow(() -> new AppException("Course not found."));
    }

    private static void validate(Course course) {
        if (course.getName() == null || course.getName().isBlank()) {
            throw new AppException("Course name is required.");
        }
        if (course.getDuration() <= 0) {
            throw new AppException("Duration must be greater than 0.");
        }
        if (course.getInstructor() == null || course.getInstructor().isBlank()) {
            throw new AppException("Instructor is required.");
        }
    }
}
