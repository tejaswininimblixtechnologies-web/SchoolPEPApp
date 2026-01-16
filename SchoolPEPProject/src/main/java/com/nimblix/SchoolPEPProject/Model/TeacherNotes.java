package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teacher_notes")
@Data
public class TeacherNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String fileUrl;

    private Long subjectId;
    private String subjectName;

    private Long classId;

    private String unitName;

    private Long studentId;
}
