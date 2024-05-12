package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_NOTAS_FISCAIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_NOTAS_FISCAIS_SEQ")
    @SequenceGenerator(name = "TB_NOTAS_FISCAIS_SEQ", sequenceName = "TB_NOTAS_FISCAIS_SEQ", allocationSize = 1)
    @Column(name = "ID_NOTAS_FISCAIS")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private User user;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "valor")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private NotaFiscalStatus status;

    @Column(name = "file_path")
    private String filePath;

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
