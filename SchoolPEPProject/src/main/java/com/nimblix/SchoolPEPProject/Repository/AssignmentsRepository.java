package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Assignments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentsRepository extends JpaRepository<Assignments,Long> {

    Optional<Assignments> findByIdAndSubjectId(Long id, Long subjectId);
    List<Assignments> findByAssignedToUserId(Long studentId);

    long countByAssignedToUserIdAndStatus(
            Long studentId,
            String status
    );

}
