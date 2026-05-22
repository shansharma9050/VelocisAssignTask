package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.assignmentofice.model.WeeklyPlanModel;

@Repository
public interface WeeklyPlannerRepository extends JpaRepository<WeeklyPlanModel, Long>  {

}
