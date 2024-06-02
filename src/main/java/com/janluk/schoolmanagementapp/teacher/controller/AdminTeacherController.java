package com.janluk.schoolmanagementapp.teacher.controller;

import com.janluk.schoolmanagementapp.common.schema.SchoolClassRequest;
import com.janluk.schoolmanagementapp.teacher.schema.CreateTeacherRequest;
import com.janluk.schoolmanagementapp.teacher.service.AdminTeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("${api.prefix}/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTeacher(@RequestBody CreateTeacherRequest request) {
        return ResponseEntity.status(CREATED).body(adminTeacherService.createTeacher(request));
    }

    @PostMapping(path = "/{id}/tutor", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignTutorToTeacher(
            @PathVariable UUID id,
            @Valid @RequestBody SchoolClassRequest request
    ) {
        return ResponseEntity.ok(adminTeacherService.assignTutorToTeacher(id, request));
    }

    @DeleteMapping(path = "/{id}/tutor")
    public ResponseEntity<Void> removeTutorAssignment(
            @PathVariable UUID id
    ) {
        adminTeacherService.removeTutorAssignment(id);

        return ResponseEntity.noContent().build();
    }
}
