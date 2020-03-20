package com.envision.Staffing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.envision.Staffing.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
//	User findByUsername(String username);
}
