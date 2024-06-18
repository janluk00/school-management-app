package com.janluk.schoolmanagementapp.subject.controller;

import com.janluk.schoolmanagementapp.subject.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.subject.schema.SchoolSubjectDTO;
import com.janluk.schoolmanagementapp.subject.service.AdminSchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;


@RestController
@RequestMapping("${api.prefix}/admin/subjects")
@RequiredArgsConstructor
public class AdminSchoolSubjectController {

    private final AdminSchoolSubjectService adminSchoolSubjectService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SchoolSubjectDTO>> getAllSchoolSubjects(Pageable pageable) {
        return ResponseEntity.ok(adminSchoolSubjectService.getAllSchoolSubjects(pageable));
    }

    @PostMapping(path = "/courses",consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody AssignTeacherToCourseRequest request) {
        return ResponseEntity.ok(adminSchoolSubjectService.assignTeacherToCourse(request));
    }
}
