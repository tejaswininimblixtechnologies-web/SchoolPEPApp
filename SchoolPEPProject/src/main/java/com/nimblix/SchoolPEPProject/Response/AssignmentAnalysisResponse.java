package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentAnalysisResponse {

    private List<AssignmentStatus> assignments;
    private Double completionPercentage;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AssignmentStatus {
        private Long subjectId;
        private boolean submitted;
        private boolean pending;
        private boolean late;
    }
}
