package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_PARAMETERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "nome", "valor"})
public class Parametro {
    @Id
    @Column(name="ID_PARAMETER")
    private Long id;

    @Column(name="NOME", nullable = false)
    private String nome;

    @Column(name="VALOR", nullable = false)
    private String valor;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private ParametroStatus status;

    @Column(name="CREATE_DATETIME")
    private LocalDateTime createDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CREATE_USER_ID")
    private User createUser;

    @Column(name="UPDATE_DATETIME")
    private LocalDateTime updateDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UPDATE_USER_ID")
    private User updateUser;

    public void update(UpdateParametroDTO dto, User updateUser) {
        this.nome = dto.nome();
        this.valor = dto.valor();

        this.setUpdateUser(updateUser);
        this.setUpdateDatetime(LocalDateTime.now());
    }

}
