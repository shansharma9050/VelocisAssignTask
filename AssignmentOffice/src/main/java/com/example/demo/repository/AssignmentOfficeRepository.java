package com.example.demo.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.assignmentofice.model.AssignmentOfficeModel;

@Repository
public interface AssignmentOfficeRepository extends JpaRepository<AssignmentOfficeModel, Long> {

	/*
	 * @Query( value = "SELECT * FROM tasks WHERE status = :status", nativeQuery =
	 * true ) List<AssignmentOfficeModel> findTasksByStatusNative(@Param("status")
	 * String status);
	 */
	

}
