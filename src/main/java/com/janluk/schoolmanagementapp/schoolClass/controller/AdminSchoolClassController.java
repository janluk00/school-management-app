package com.janluk.schoolmanagementapp.schoolClass.controller;

import com.janluk.schoolmanagementapp.schoolClass.schema.SchoolClassDTO;
import com.janluk.schoolmanagementapp.schoolClass.service.AdminSchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/admin/school-classes")
@RequiredArgsConstructor
class AdminSchoolClassController {

    private final AdminSchoolClassService adminSchoolClassService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SchoolClassDTO>> getAllSchoolClasses(Pageable pageable) {
        return ResponseEntity.ok(adminSchoolClassService.getAllSchoolClasses(pageable));
    }
}
