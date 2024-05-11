package com.ferraz.controledepagamentosbackend.domain.link;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_LINKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Link {

    @Id
    @Column(name = "ID_LINK")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HORA_EXTRA")
    private HorasExtras horasExtras;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACAO")
    private AcaoLink acao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private LinkStatus status;

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

    public Link(HorasExtras horasExtras, AcaoLink acao, User createUser) {
        this.id = UUID.randomUUID();
        this.horasExtras = horasExtras;
        this.acao = acao;

        this.status = LinkStatus.CRIADO;
        this.createUser = createUser;
        this.createDatetime = LocalDateTime.now();
    }

    public void atualizar(LinkStatus status, User updateUser) {
        this.status = status;
        this.updateUser = updateUser;
        this.updateDatetime = LocalDateTime.now();
    }

    public void marcarComoUsado() {
        this.status = LinkStatus.USADO;
        this.updateDatetime = LocalDateTime.now();
    }
}
