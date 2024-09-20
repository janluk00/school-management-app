package factory;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;

import java.util.Set;

public class StudentFactory {

    public static StudentEntity aStudentInClass(ClassType classType) {
        SchoolClassEntity studentSchoolClass = SchoolClassEntity.builder()
                .name(classType.name())
                .build();

        return StudentEntity.builder()
                .schoolClass(studentSchoolClass)
                .build();
    }

    public static StudentEntity aStudentWithUserInClass(String email, ClassType classType) {
        SchoolClassEntity studentSchoolClass = SchoolClassEntity.builder()
                .name(classType.name())
                .build();

        return StudentEntity.builder()
                .user(UserFactory.aStudentUser(email))
                .schoolClass(studentSchoolClass)
                .build();
    }

    public static StudentEntity aStudentWithUser(String email) {
        return StudentEntity.builder()
                .user(UserFactory.aStudentUser(email))
                .schoolClass(SchoolClassFactory.aSchoolClassA1())
                .build();
    }

    public static StudentEntity aStudentWithUserAndGrade(String email, Set<GradeEntity> grades) {
        return StudentEntity.builder()
                .user(UserFactory.aStudentUser(email))
                .schoolClass(SchoolClassFactory.aSchoolClassA1())
                .grades(grades)
                .build();
    }

    public static CreateStudentRequest aCreateStudentRequestWithEmail(String email) {
        return new CreateStudentRequest(ClassType.A1, UserFactory.aCreateUserRequestWithEmail(email));
    }
}
