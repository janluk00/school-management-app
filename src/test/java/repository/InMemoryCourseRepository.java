package repository;

import com.janluk.schoolmanagementapp.common.model.CourseEntity;
import com.janluk.schoolmanagementapp.common.repository.port.CourseRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryCourseRepository implements CourseRepository {

    private final Map<UUID, CourseEntity> courses = new HashMap<>();

    @Override
    public Optional<CourseEntity> getByTeacherAndSchoolSubject(UUID teacherId, String schoolSubject) {
        return courses.values().stream()
                .filter(tic -> tic.getTeacher().getId().equals(teacherId) &&
                        tic.getSubject().getName().equals(schoolSubject))
                .findFirst();
    }

    @Override
    public UUID save(CourseEntity course) {
        UUID courseId = course.getId();

        courses.put(course.getId(), course);

        return courseId;
    }

    public CourseEntity getById(UUID id) {
        CourseEntity course =  courses.get(id);

        if (course == null) {
            throw new IllegalArgumentException();
        }

        return course;
    }

    public void deleteAll() {
        courses.clear();
    }
}
