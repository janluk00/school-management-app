package com.janluk.schoolmanagementapp.student.controller;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentRequest;
import com.janluk.schoolmanagementapp.student.schema.CreateStudentResponse;
import com.janluk.schoolmanagementapp.student.service.AdminStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StudentDTO>> searchStudents(
            @ModelAttribute CommonUserFilters filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(adminStudentService.searchStudents(filters, pageable));
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminStudentService.getStudentById(id));
    }

    @GetMapping(path = "/school-classes/{schoolClass}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentDTO>> getAllStudentsInClass(@PathVariable ClassType schoolClass) {
        return ResponseEntity.ok(adminStudentService.getAllStudentsInClass(schoolClass));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateStudentResponse> createStudent(@RequestBody @Valid CreateStudentRequest request) {
        return ResponseEntity.status(CREATED).body(adminStudentService.createStudent(request));
    }
}
