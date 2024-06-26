package com.ferraz.controledepagamentosbackend.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByEmail(@NotBlank @Email String email);

	List<User> findByPerfilAndStatusOrderByNome(UsuarioPerfil perfil, UserStatus status);
	
	
	@Query("SELECT u FROM User u " +
		       "WHERE (:nome IS NULL OR UPPER(u.nome) like CONCAT('%', UPPER(cast(:nome AS text)),'%')) " +
		       "AND (:email IS NULL OR UPPER(u.email) like CONCAT('%', UPPER(cast(:email AS text)),'%')) " +
		       "AND (:perfil IS NULL OR u.perfil = :perfil) " +
		       "AND (:status IS NULL OR u.status = :status)")
	Page<User> findByFiltros(Pageable page, 
			@Param("nome") String nome, @Param("email") String email, @Param("perfil")UsuarioPerfil perfil,
			@Param("status")UserStatus status);
}
