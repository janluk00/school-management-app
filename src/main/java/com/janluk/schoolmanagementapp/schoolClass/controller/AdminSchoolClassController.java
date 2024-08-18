package com.janluk.schoolmanagementapp.schoolClass.controller;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.StudentDTO;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectInCourseDTO;
import com.janluk.schoolmanagementapp.schoolClass.schema.AssignTeacherToCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.RemoveTeacherFromCourseRequest;
import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminCourseAssignmentService;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminSchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/admin/classes")
@RequiredArgsConstructor
class AdminSchoolClassController {

    private final AdminSchoolClassService adminSchoolClassService;
    private final AdminCourseAssignmentService adminCourseAssignmentService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SchoolClassDTO>> getAllSchoolClasses(Pageable pageable) {
        return ResponseEntity.ok(adminSchoolClassService.getAllSchoolClasses(pageable));
    }

    @GetMapping(path = "/{schoolClass}/students", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentDTO>> getAllStudentsInClass(@PathVariable ClassType schoolClass) {
        return ResponseEntity.ok(adminSchoolClassService.getAllStudentsInClass(schoolClass));
    }

    @GetMapping(path = "/{schoolClass}/courses", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaughtSubjectInCourseDTO>> getAllCoursesForClass(@PathVariable ClassType schoolClass) {
        return ResponseEntity.ok(adminSchoolClassService.getAllCoursesForClass(schoolClass));
    }

    @PostMapping(path = "/courses", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody AssignTeacherToCourseRequest request) {
        return ResponseEntity.ok(adminCourseAssignmentService.assignTeacherToCourse(request));
    }

    @DeleteMapping(path = "/courses", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeTeacherFromCourse(@RequestBody RemoveTeacherFromCourseRequest request) {
        adminCourseAssignmentService.removeTeacherFromCourse(request);

        return ResponseEntity.noContent().build();
    }
}
