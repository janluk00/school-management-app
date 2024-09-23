package com.janluk.schoolmanagementapp.course.controller;

import com.janluk.schoolmanagementapp.course.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.course.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.course.service.AdminCourseAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseAssignmentService adminCourseAssignmentService;

    @PostMapping(path = "", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody @Valid AssignTeacherToCourseRequest request) {
        return ResponseEntity.ok(adminCourseAssignmentService.assignTeacherToCourse(request));
    }

    @DeleteMapping(path = "", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeTeacherFromCourse(@RequestBody @Valid RemoveTeacherFromCourseRequest request) {
        return ResponseEntity.ok(adminCourseAssignmentService.removeTeacherFromCourse(request));
    }
}
