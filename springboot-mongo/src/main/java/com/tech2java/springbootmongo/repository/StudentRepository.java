package com.tech2java.springbootmongo.repository;

import com.tech2java.springbootmongo.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student,Long> {


}
