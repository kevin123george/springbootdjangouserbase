package com.example.authtry.repository;

import com.example.authtry.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<ApplicationUser, String> {

    ApplicationUser findByUsername(String username);

}
