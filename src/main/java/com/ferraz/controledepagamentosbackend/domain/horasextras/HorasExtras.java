package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_HORAS_EXTRAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class HorasExtras {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_HORAS_EXTRAS_SEQ")
    @SequenceGenerator(name = "TB_HORAS_EXTRAS_SEQ", sequenceName = "TB_HORAS_EXTRAS_SEQ", allocationSize = 1)
    @Column(name = "ID_HORAS_EXTRAS")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private User user;

    @Column(name = "DATA_HORA_INICIO")
    private LocalDateTime dataHoraInicio;

    @Column(name = "DATA_HORA_FIM")
    private LocalDateTime dataHoraFim;

    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_APROVADOR")
    private User aprovador;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private HorasExtrasStatus status;

    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_USER_ID")
    private User createUser;

    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_USER_ID")
    private User updateUser;

    public HorasExtras(NovasHorasExtrasDTO dto, User user, User aprovador) {
        this.dataHoraInicio = dto.dataHoraInicio();
        this.dataHoraFim = dto.dataHoraFim();
        this.descricao = dto.descricao();
        this.aprovador = aprovador;

        this.status = HorasExtrasStatus.SOLICITADO;
        this.createDatetime = LocalDateTime.now();
        this.createUser = user;
        this.user = user;
    }

    public void update(AtualizarHorasExtrasDTO dto, User loggedUser, User aprovador) {
        this.dataHoraInicio = dto.dataHoraInicio();
        this.dataHoraFim = dto.dataHoraFim();
        this.descricao = dto.descricao();
        this.aprovador = aprovador;

        this.updateDatetime = LocalDateTime.now();
        this.updateUser = loggedUser;
    }

    public void inativar(User loggedUser) {
        this.status = HorasExtrasStatus.INATIVO;
        this.updateUser = loggedUser;
        this.updateDatetime = LocalDateTime.now();
    }
}
