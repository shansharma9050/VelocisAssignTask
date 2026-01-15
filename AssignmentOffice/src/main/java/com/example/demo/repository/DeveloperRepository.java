package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.assignmentofice.model.DeveloperModel;

@Repository
public interface DeveloperRepository extends JpaRepository<DeveloperModel, Long> {

}
