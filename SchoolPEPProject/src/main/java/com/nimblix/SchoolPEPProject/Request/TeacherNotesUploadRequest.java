package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class TeacherNotesUploadRequest {

    private String title;
    private String description;
    private String fileUrl;

    private Long subjectId;
    private String subjectName;

    private Long classId;

    private String unitName;

    private Long studentId;
}
