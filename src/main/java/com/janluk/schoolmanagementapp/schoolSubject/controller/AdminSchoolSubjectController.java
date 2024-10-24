package com.janluk.schoolmanagementapp.schoolSubject.controller;

import com.janluk.schoolmanagementapp.common.model.vo.ClassType;
import com.janluk.schoolmanagementapp.common.schema.TaughtSubjectDTO;
import com.janluk.schoolmanagementapp.schoolSubject.schema.SchoolSubjectDTO;
import com.janluk.schoolmanagementapp.schoolSubject.service.AdminSchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("${api.prefix}/admin/school-subjects")
@RequiredArgsConstructor
class AdminSchoolSubjectController {

    private final AdminSchoolSubjectService adminSchoolSubjectService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SchoolSubjectDTO>> getAllSchoolSubjects(Pageable pageable) {
        return ResponseEntity.ok(adminSchoolSubjectService.getAllSchoolSubjects(pageable));
    }

    @GetMapping(path = "/school-classes/{schoolClass}/courses", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaughtSubjectDTO>> getAllCoursesForClass(@PathVariable ClassType schoolClass) {
        return ResponseEntity.ok(adminSchoolSubjectService.getAllTaughtSubjectsInClass(schoolClass));
    }
}
