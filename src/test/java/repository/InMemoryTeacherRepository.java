package repository;

import com.janluk.schoolmanagementapp.common.exception.NoResultFoundException;
import com.janluk.schoolmanagementapp.common.model.*;
import com.janluk.schoolmanagementapp.common.repository.port.TeacherRepository;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTeacherRepository implements TeacherRepository {

    private final Map<UUID, TeacherEntity> teachers = new HashMap<>();

    @Override
    public TeacherEntity getById(UUID id) {
        TeacherEntity teacher = teachers.get(id);

        if (teacher == null) {
            throw new NoResultFoundException("Could not find teacher with id: %s".formatted(id.toString()));
        }

        return teacher;
    }

    @Override
    public TeacherEntity getByEmail(String email) {
        return teachers.values().stream()
                .filter(teacher -> teacher.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoResultFoundException(
                                "Could not find teacher with email: %s".formatted(email)
                        )
                );
    }

    @Override
    public List<CourseDTO> getAllCoursesByTeacher(UUID id) {
        TeacherEntity teacher = teachers.get(id);

        if (teacher == null) {
            return Collections.emptyList();
        }

        Set<CourseEntity> teacherCourses = teacher.getCourses();

        return teacherCourses.stream()
                .flatMap(course -> course.getSchoolClasses().stream()
                        .map(schoolClass ->
                                new CourseDTO(schoolClass.getName(), course.getSubject().getName())))
                .toList();
    }

    @Override
    public List<CourseDTO> getAllCoursesByTeacher(String email) {
        TeacherEntity teacher = teachers.values().stream()
                .filter(t -> t.getUser().getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (teacher == null) {
            return Collections.emptyList();
        }

        Set<CourseEntity> courses = teacher.getCourses();

        return courses.stream()
                .flatMap(course -> course.getSchoolClasses().stream()
                        .map(schoolClass ->
                                new CourseDTO(schoolClass.getName(), course.getSubject().getName())))
                .distinct()
                .toList();
    }

    @Override
    public List<StudentPerformanceReportDTO> getStudentsPerformanceReportByTeacher(String email) {
        TeacherEntity teacher = teachers.values().stream()
                .filter(t -> t.getUser().getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (teacher == null) {
            return Collections.emptyList();
        }

        Set<CourseEntity> courses = teacher.getCourses();

        return courses.stream()
                .flatMap(course -> course.getSchoolClasses().stream()
                        .flatMap(schoolClass -> schoolClass.getStudents().stream()
                                .flatMap(student -> student.getGrades().stream()
                                        .filter(grade -> grade.getSubjectName().equals(course.getSubject().getName()))
                                        .map(grade -> (
                                                new AbstractMap.SimpleEntry<>(
                                                        new StudentPerformanceKey(
                                                                schoolClass.getName(),
                                                                course.getSubject().getName()
                                                        ),
                                                        grade.getGrade().doubleValue()
                                                )
                                        ))
                                )
                        )
                )
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.averagingDouble(Map.Entry::getValue)
                        )
                )
                .entrySet().stream()
                .map(entry -> new StudentPerformanceReportDTO(
                                entry.getKey().schoolClass(),
                                entry.getKey().schoolSubject(),
                                entry.getValue()
                        )
                )
                .distinct()
                .toList();
    }


    @Override
    public Page<TeacherEntity> getAll(Specification<TeacherEntity> spec, Pageable pageable) {
        throw new NotImplementedTestMethodException();
    }

    @Override
    public UUID save(TeacherEntity teacher) {
        UUID teacherId = teacher.getId();

        teachers.put(teacherId, teacher);

        return teacherId;
    }

    public void deleteAll() {
        teachers.clear();
    }
}

record StudentPerformanceKey(
        String schoolClass,
        String schoolSubject
) {}
