package com.nimblix.SchoolPEPProject.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentPerformanceSummaryResponse {

    private double academicScore;
    private double attendancePercentage;
    private double assignmentCompletionPercentage;
}

