package com.janluk.schoolmanagementapp.teacher.controller;

import com.janluk.schoolmanagementapp.common.criteria.CommonUserFilters;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;
import com.janluk.schoolmanagementapp.common.schema.SchoolSubjectRequest;
import com.janluk.schoolmanagementapp.teacher.schema.*;
import com.janluk.schoolmanagementapp.teacher.service.AdminTeacherAssignmentService;
import com.janluk.schoolmanagementapp.teacher.service.AdminTeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("${api.prefix}/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;
    private final AdminTeacherAssignmentService adminTeacherAssignmentService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TeacherDTO>> searchTeachers(
            @ModelAttribute CommonUserFilters filters,
            Pageable pageable
    ) {
        return ResponseEntity.ok(adminTeacherService.searchTeachers(filters, pageable));
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminTeacherService.getTeacherById(id));
    }

    @GetMapping(path = "/{id}/courses", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCoursesByTeacherId(@PathVariable UUID id) {
        return ResponseEntity.ok(adminTeacherService.getAllCoursesByTeacherId(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateTeacherResponse> createTeacher(@RequestBody @Valid CreateTeacherRequest request) {
        return ResponseEntity.status(CREATED).body(adminTeacherService.createTeacher(request));
    }

    @PostMapping(path = "/{id}/tutor", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignTutorToTeacherResponse> assignTutorToTeacher(
            @PathVariable UUID id,
            @RequestBody @Valid SchoolClassRequest request
    ) {
        return ResponseEntity.ok(adminTeacherAssignmentService.assignTutorToTeacher(id, request));
    }

    @PostMapping(path = "/{id}/school-subjects", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignSubjectToTeacherResponse> assignSubjectToTeacher(
            @PathVariable UUID id,
            @RequestBody @Valid SchoolSubjectRequest request
    ) {
        return ResponseEntity.ok(adminTeacherAssignmentService.assignSubjectToTeacher(id, request));
    }

    @DeleteMapping(path = "/{id}/tutor")
    public ResponseEntity<RemoveTutorAssignmentResponse> removeTutorAssignment(@PathVariable UUID id) {
        return ResponseEntity.ok(adminTeacherAssignmentService.removeTutorAssignment(id));
    }

    @DeleteMapping(path = "/{id}/school-subjects")
    public ResponseEntity<RemoveSubjectFromTeacherResponse> removeSubjectFromTeacher(
            @PathVariable UUID id,
            @RequestParam SubjectType subject
    ) {
        return ResponseEntity.ok(adminTeacherAssignmentService.removeSubjectFromTeacher(id, subject));
    }
}
