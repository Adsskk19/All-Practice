package com.taskproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskproject.entity.Users;

public interface UserRepository extends JpaRepository<Users,Long> {

	
}
