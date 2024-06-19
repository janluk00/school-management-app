package com.janluk.schoolmanagementapp.schoolClass.controller;

import com.janluk.schoolmanagementapp.schoolClass.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminSchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/admin/classes")
@RequiredArgsConstructor
public class AdminSchoolClassController {

    private final AdminSchoolClassService adminSchoolClassService;

    @PostMapping(path = "/courses", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody AssignTeacherToCourseRequest request) {
        return ResponseEntity.ok(adminSchoolClassService.assignTeacherToCourse(request));
    }

    @DeleteMapping(path = "/courses", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeTeacherFromCourse(@RequestBody RemoveTeacherFromCourseRequest request) {
        adminSchoolClassService.removeTeacherFromCourse(request);

        return ResponseEntity.noContent().build();
    }
}
