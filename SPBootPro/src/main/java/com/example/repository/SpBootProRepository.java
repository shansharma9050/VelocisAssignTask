package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.common.model.SpBootProModel;


@Repository
public interface SpBootProRepository extends JpaRepository<SpBootProModel, Long> {
	
	@Query(value = "select * from userdetails where id=?",nativeQuery = true)
	Optional<SpBootProModel> getUserDetails(Long id);
	
	@Query(value = "select * from userdetails where isdeleted=0",nativeQuery = true)
	List<SpBootProModel> getUserData();
	
	@Query(value = "select * from userdetails where email=:email",nativeQuery = true)
	Optional<SpBootProModel> findByEmail(@Param("email") String email);

}
