package com.ferraz.controledepagamentosbackend.domain.notificacao;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_NOTIFICACOES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_NOTIFICACOES_SEQ")
    @SequenceGenerator(name = "TB_NOTIFICACOES_SEQ", sequenceName = "TB_NOTIFICACOES_SEQ", allocationSize = 1)
    @Column(name = "ID_NOTIFICACAO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private User user;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "PATH")
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private NotificacaoStatus status;

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

}
