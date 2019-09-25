package com.eslab.student.controller;

import com.eslab.student.model.Student;
import com.eslab.student.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) throws IOException {
        Student createdStudent = studentService.create(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.OK);
    }

    @GetMapping("/{rollNo}")
    public ResponseEntity<Student> findByRollNo(@PathVariable("rollNo") String rollNumber) {
        Student student = studentService.findByRollNumber(rollNumber);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        List<Student> students = new ArrayList<>();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Student> update(@RequestBody Student student) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> delete(@PathVariable("id") String id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
