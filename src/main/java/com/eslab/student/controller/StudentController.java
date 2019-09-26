package com.eslab.student.controller;

import com.eslab.student.model.Student;
import com.eslab.student.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") String id) {
        Student student = studentService.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Student> findByRollNo(@RequestParam("rollNo") String rollNumber) {
        Student student = studentService.findByRollNumber(rollNumber);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Student> update(@RequestBody Student student) throws IOException {
        Student createdStudent = studentService.update(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") String id) {
        String deleteById = studentService.deleteById(id);
        return new ResponseEntity<>(deleteById, HttpStatus.OK);
    }


}
