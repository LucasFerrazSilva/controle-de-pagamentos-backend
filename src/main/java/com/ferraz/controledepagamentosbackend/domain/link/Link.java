package com.ferraz.controledepagamentosbackend.domain.link;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LINKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_LINKS_SEQ")
    @SequenceGenerator(name = "TB_LINKS_SEQ", sequenceName = "TB_LINKS_SEQ", allocationSize = 1)
    @Column(name = "ID_LINK")
    private Long id;

    @Column(name = "LINK")
    private String hash;

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

    public Link(String hash, HorasExtras horasExtras, AcaoLink acao, User createUser) {
        this.hash = hash;
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

}
