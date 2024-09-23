package factory;

import com.janluk.schoolmanagementapp.common.model.RoleEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.vo.RoleType;
import com.janluk.schoolmanagementapp.common.schema.CreateUserRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

public class UserFactory {

    public static UserEntity aTeacherUser(String email) {
        return UserEntity.builder()
                .name("John")
                .surname("Doe")
                .email(email)
                .password("XXXXYYYY")
                .roles(new HashSet<>(Collections.singleton(new RoleEntity(RoleType.ROLE_TEACHER.name()))))
                .birthDate(LocalDate.of(1999, 5, 15))
                .passwordConfirmationToken("XXX")
                .build();
    }

    public static CreateUserRequest aCreateUserRequestWithEmail(String email) {
        return new CreateUserRequest("Kevin", "Spacey", email, LocalDate.of(1999, 7, 12));
    }

    public static UserEntity aStudentUser(String email) {
        return UserEntity.builder()
                .name("David")
                .surname("Mills")
                .email(email)
                .password("XXXXYYYY")
                .roles(new HashSet<>(Collections.singleton(new RoleEntity(RoleType.ROLE_STUDENT.name()))))
                .birthDate(LocalDate.of(1999, 5, 15))
                .passwordConfirmationToken("XXX")
                .build();
    }
}
