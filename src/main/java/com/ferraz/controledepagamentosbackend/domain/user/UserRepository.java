package com.ferraz.controledepagamentosbackend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByEmail(@NotBlank @Email String email);

}
