package com.ferraz.controledepagamentosbackend.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "email", "name"})
public class User implements UserDetails {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_USERS_SEQ")
    @SequenceGenerator(name = "TB_USERS_SEQ", sequenceName = "TB_USERS_SEQ", allocationSize = 1)
    @Column(name="ID_USER")
    private Long id;

    @Column(name="NOME", nullable = false)
    private String nome;

    @Column(name="EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name="SENHA", nullable = false)
    private String senha;

    @Column(name="SALARIO", nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    @Column(name="PERFIL", nullable = false)
    private UsuarioPerfil perfil;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private UserStatus status;

    @Column(name="CREATE_DATETIME")
    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CREATE_USER_ID")
    private User createUser;

    @Column(name="UPDATE_DATETIME")
    private LocalDateTime updateDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UPDATE_USER_ID")
    private User updateUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.perfil.toString()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ATIVO;
    }

	public User(DadosCreateUserDTO dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.salario = dados.salario();
		this.perfil = dados.perfil();
		this.status = UserStatus.ATIVO;
		this.createDateTime = LocalDateTime.now();

	}
	
	public void atualizar(DadosAtualizacaoUserDTO dados) {
		if(dados.nome() != null) {
			this.nome = dados.nome();
		}
		if(dados.email() != null) {
			this.email = dados.email();
		}
		if(dados.salario() != null) {
			this.salario = dados.salario();
		}
		if(dados.perfil() != null) {
			this.perfil = dados.perfil();
		}
	}

    public void mudarSenha(String encondedPassword, User user){
        this.setSenha(encondedPassword);
        update(user);
    }

    public void deactivate(User user){
        this.setStatus(UserStatus.INATIVO);
        update(user);
    }

    public void update(User user){
        this.setUpdateUser(user);
        this.setUpdateDatetime(LocalDateTime.now());
    }
}
