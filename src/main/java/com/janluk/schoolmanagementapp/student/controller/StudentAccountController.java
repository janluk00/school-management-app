package com.janluk.schoolmanagementapp.student.controller;

import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradePointAverageDTO;
import com.janluk.schoolmanagementapp.student.schema.SchoolSubjectGradesDTO;
import com.janluk.schoolmanagementapp.student.service.StudentAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/students")
@RequiredArgsConstructor
class StudentAccountController {

    private final StudentAccountService studentAccountService;

    @GetMapping(path = "/grades", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SchoolSubjectGradesDTO>> getGradesGroupedBySchoolSubject(Principal principal) {
        return ResponseEntity.ok(studentAccountService.getGradesGroupedBySchoolSubject(principal.getName()));
    }

    @GetMapping(path = "/grades/averages", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SchoolSubjectGradePointAverageDTO>> getGradePointAveragesGroupedBySchoolSubject(
            Principal principal
    ) {
        return ResponseEntity.ok(
                studentAccountService.getGradePointAveragesGroupedBySchoolSubject(principal.getName())
        );
    }

    @GetMapping(path = "/courses", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaughtSubjectDTO>> getAllCoursesByStudent(Principal principal) {
        return ResponseEntity.ok(studentAccountService.getAllCoursesByStudent(principal.getName()));
    }
}
