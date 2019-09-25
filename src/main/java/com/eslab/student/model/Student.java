package com.eslab.student.model;

import com.eslab.common.Model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Student implements Model {

    private long rollNo;
    private String firstName;
    private String lastName;
    private String department;
    private byte year;
    private String section;
    private LocalDate dateOfBirth;
    private List<String> subjects;
    private List<String> projects;
}
