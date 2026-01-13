package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardResponse {
    private StudentPerformanceSummaryResponse summary;
    private List<SubjectAcademicResponse> academics;
    private AttendanceWeeklyResponse attendance;
    private AssignmentAnalysisResponse assignments;
}
