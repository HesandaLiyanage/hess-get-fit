package com.hess.fitnessactivityservice.repository;

import com.hess.fitnessactivityservice.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(@NotBlank(message = "email is required") String email);
}
