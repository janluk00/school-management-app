package com.janluk.schoolmanagementapp.teacher.controller;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.model.vo.SubjectType;
import com.janluk.schoolmanagementapp.common.schema.CourseDTO;
import com.janluk.schoolmanagementapp.teacher.schema.AddGradeRequest;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceDTO;
import com.janluk.schoolmanagementapp.teacher.schema.StudentPerformanceReportDTO;
import com.janluk.schoolmanagementapp.teacher.service.TeacherAccountService;
import com.janluk.schoolmanagementapp.teacher.service.TeacherStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/teachers")
@RequiredArgsConstructor
public class TeacherAccountController {

    private final TeacherAccountService teacherAccountService;
    private final TeacherStatisticsService teacherStatisticsService;

    @GetMapping(path = "/courses", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCourses(Principal principal) {
        return ResponseEntity.ok(teacherAccountService.getAllCoursesByTeacherEmail(principal.getName()));
    }

    @GetMapping(path = "/classes/{schoolClass}/students", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentPerformanceDTO>> getAllStudentsInCourse(
            @PathVariable ClassType schoolClass,
            @RequestParam SubjectType subject,
            Principal principal
    ) {
        return ResponseEntity.ok(
                teacherAccountService.getAllStudentsInCourse(schoolClass, subject, principal.getName())
        );
    }

    @GetMapping(path = "/reports", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentPerformanceReportDTO>> getStudentsPerformanceReportByTeacher(
            Principal principal
    ) {
        return ResponseEntity.ok(teacherStatisticsService.getStudentsPerformanceReportByTeacher(principal.getName()));
    }

    @PostMapping(path = "/students/{studentId}/grades")
    public ResponseEntity<String> addGrade(
            @PathVariable UUID studentId,
            @RequestBody AddGradeRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(teacherAccountService.addGrade(studentId, request, principal.getName()));
    }
}