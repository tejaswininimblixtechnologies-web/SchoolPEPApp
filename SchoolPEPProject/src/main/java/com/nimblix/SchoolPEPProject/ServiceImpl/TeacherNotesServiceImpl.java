package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.TeacherNotes;
import com.nimblix.SchoolPEPProject.Repository.TeacherNotesRepository;
import com.nimblix.SchoolPEPProject.Request.TeacherNotesUploadRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherNotesServiceImpl implements TeacherNotesService {

    private final TeacherNotesRepository teacherNotesRepository;

    @Override
    public String uploadNotes(TeacherNotesUploadRequest request) {


        if (request.getSubjectId() == null || request.getClassId() == null) {
            throw new RuntimeException("subjectId and classId are mandatory");
        }

        TeacherNotes notes = new TeacherNotes();
        notes.setTitle(request.getTitle());
        notes.setDescription(request.getDescription());
        notes.setFileUrl(request.getFileUrl());
        notes.setSubjectId(request.getSubjectId());
        notes.setSubjectName(request.getSubjectName());
        notes.setClassId(request.getClassId());
        notes.setUnitName(request.getUnitName());
        notes.setStudentId(request.getStudentId());

        teacherNotesRepository.save(notes);

        return "Notes uploaded successfully";
    }
}
