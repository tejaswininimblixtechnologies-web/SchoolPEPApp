package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(
        name = "attendance_record",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "attendance_date"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "attendance_date", nullable = false)
    private String attendanceDate;

    @Column(name = "attendance_status", nullable = false)
    private String attendanceStatus;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;


    @PrePersist
    protected void onCreate(){
        createdTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();

    }

    @PreUpdate
    protected void onUpdate(){
        updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();


    }

}