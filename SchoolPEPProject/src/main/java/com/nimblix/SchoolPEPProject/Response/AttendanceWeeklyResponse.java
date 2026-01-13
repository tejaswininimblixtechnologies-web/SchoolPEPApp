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
public class AttendanceWeeklyResponse {

    private String week;
    private List<DayAttendance> attendance;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DayAttendance {
        private String day;
        private boolean present;
    }
}
