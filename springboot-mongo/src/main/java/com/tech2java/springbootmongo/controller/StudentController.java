package com.tech2java.springbootmongo.controller;

import com.tech2java.springbootmongo.model.DatabaseSequence;
import com.tech2java.springbootmongo.model.Student;
import com.tech2java.springbootmongo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student){
        System.out.println("inside addStudent---");
        student.setId(generateSequence(Student.SEQUENCE_NAME));
        studentRepository.save(student);
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable(value = "id") Long id){
        System.out.println("inside getStudent---"+id);
        Student student= studentRepository.findById(id).orElse(null);
        System.out.println("Student::"+student);
        return student;
    }

    @GetMapping("/fetchStudent")
    public List<Student> getAllStudents(){
        System.out.println("inside getAllStudents---");
        return studentRepository.findAll();
    }

    @PutMapping("/updateStudent")
    public void updateStudent(@RequestBody Student student){
        System.out.println("inside updateStudent---");
        Student existingStudent=studentRepository.findById(student.getId()).orElse(null);

        if(existingStudent!=null){
            existingStudent.setAddress(student.getAddress());
            existingStudent.setName(student.getName());
            studentRepository.save(existingStudent);
        }else{
            System.out.println("No Student Available with roll number: "+student.getId());
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable(value="id") Long id){
        System.out.println("inside deleteStudent---");
        studentRepository.deleteById(id);
    }


    @PostMapping("/addStudentList")
    public void addStudents(@RequestBody List<Student> student){
        System.out.println("inside addStudents---");
        studentRepository.saveAll(student);
    }

    public long generateSequence(String seqName) {

        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;

    }

}
