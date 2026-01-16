package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.TeacherNotesUploadRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TeacherNotesController {

    private final TeacherNotesService teacherNotesService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadNotes(
            @RequestBody TeacherNotesUploadRequest request) {

        String response = teacherNotesService.uploadNotes(request);
        return ResponseEntity.ok(response);
    }
}
