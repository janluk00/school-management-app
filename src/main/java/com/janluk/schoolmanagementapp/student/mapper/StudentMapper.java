package com.janluk.schoolmanagementapp.student.mapper;

import com.janluk.schoolmanagementapp.common.model.GradeEntity;
import com.janluk.schoolmanagementapp.common.model.SchoolClassEntity;
import com.janluk.schoolmanagementapp.common.model.StudentEntity;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.UserBaseInformationDTO;
import com.janluk.schoolmanagementapp.common.schema.UserPersonalDTO;
import com.janluk.schoolmanagementapp.common.user.UserCreator;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;
import com.janluk.schoolmanagementapp.student.schema.GradeDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradesDTO;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StudentMapper {

    public StudentEntity createStudentRequestToStudentEntity(CreateStudentRequest createRequest) {
        return StudentEntity.builder()
                .id(UUID.randomUUID())
                .user(UserCreator.createUserEntity(createRequest.user()))
                .schoolClass(classTypeToSchoolClassEntity(createRequest.schoolClass()))
                .build();
    }

    public List<StudentDTO> studentEntitiesToStudentDTOs(List<StudentEntity> students) {
        return students.stream()
                .map(this::studentEntityToStudentDTO)
                .toList();
    }

    public Page<StudentDTO> pageStudentEntitiesToPageStudentDTOs(Page<StudentEntity> students) {
        return students.map(this::studentEntityToStudentDTO);
    }

    public StudentDTO studentEntityToStudentDTO(StudentEntity student) {
        return StudentDTO.builder()
                .id(student.getId())
                .user(userEntityToUserBaseInformationDTO(student.getUser()))
                .schoolClass(student.getSchoolClass().getName())
                .build();
    }

    public List<StudentPerformanceDTO> studentEntitiesToStudentPerformanceDTOs(List<StudentEntity> students) {
        return students.stream()
                .map(this::studentEntityToStudentPerformanceDTO)
                .toList();
    }

    public List<SchoolSubjectGradesDTO> mapToSchoolSubjectGradesDTOs(Map<String, List<GradeDTO>> grades) {
        return grades.entrySet().stream()
                .map(this::mapToSchoolSubjectGradesDTO)
                .toList();
    }

    private StudentPerformanceDTO studentEntityToStudentPerformanceDTO(StudentEntity student) {
        return StudentPerformanceDTO.builder()
                .id(student.getId())
                .user(userEntityToUserPersonalDTO(student.getUser()))
                .grades(gradeEntitiesToBigDecimals(student.getGrades()))
                .build();
    }

    private UserPersonalDTO userEntityToUserPersonalDTO(UserEntity user) {
        return UserPersonalDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }

    private List<BigDecimal> gradeEntitiesToBigDecimals(List<GradeEntity> grades) {
        return grades.stream()
                .map(GradeEntity::getGrade)
                .toList();
    }

    private UserBaseInformationDTO userEntityToUserBaseInformationDTO(UserEntity user) {
        return UserBaseInformationDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .birthDate(user.getBirthDate())
                .build();
    }

    private SchoolClassEntity classTypeToSchoolClassEntity(ClassType classType) {
        return SchoolClassEntity.builder()
                .name(classType.name())
                .build();
    }

    private SchoolSubjectGradesDTO mapToSchoolSubjectGradesDTO(Map.Entry<String, List<GradeDTO>> entry) {
        String schoolSubject = entry.getKey();

        List<BigDecimal> grades = entry.getValue().stream()
                .map(GradeDTO::grade)
                .toList();

        return SchoolSubjectGradesDTO.builder()
                .schoolSubject(schoolSubject)
                .grades(grades)
                .build();
    }
}
