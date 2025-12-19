package com.hess.fitnessuserservice.repository;

import com.hess.fitnessuserservice.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(@NotBlank(message = "email is required") String email);
}
