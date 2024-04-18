package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_PARAMETERS_SEQ")
    @SequenceGenerator(name = "TB_PARAMETERS_SEQ", sequenceName = "TB_PARAMETERS_SEQ", allocationSize = 1)
    @Column(name="ID_PARAMETERS")
    private Long id;

    @Column(name="NOME", nullable = false)
    private String nome;

    @Column(name="NOME", nullable = false)
    private String valor;

    @Column(name="STATUS")
    private ParametroStatus status;

    @Column(name="CREATE_DATETIME")
    private LocalDateTime createDatetime;

    @ManyToOne
    @JoinColumn(name="CREATE_USER_ID")
    private User createUser;

    @Column(name="UPDATE_DATETIME")
    private LocalDateTime updateDatetime;

    @ManyToOne
    @JoinColumn(name="UPDATE_USER_ID")
    private User updateUser;

    public Parametro(NovoParametroDTO novoParametroDTO, User loggedUser) {
        this.nome = novoParametroDTO.nome();
        this.valor = novoParametroDTO.valor();
        this.status = ParametroStatus.ATIVO;
        this.createUser = loggedUser;
        this.createDatetime = LocalDateTime.now();
    }
}
