package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
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
    @Column(name = "ID_NOTA_FISCAL")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private User user;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANO")
    private Integer ano;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private NotaFiscalStatus status;

    @Column(name = "FILE_PATH")
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

    public NotaFiscal(NovaNotaFiscalDTO dto, User user){
        this.mes = dto.mes();
        this.ano = dto.ano();
        this.valor = dto.valor();
        this.status = NotaFiscalStatus.SOLICITADA;

        this.createDatetime = LocalDateTime.now();
        this.createUser = createUser;
        this.user = user;
    }

    public void update(AtualizarNotaFiscalDTO dto, User user){
        this.mes = dto.mes();
        this.ano = dto.ano();
        this.valor = dto.valor();
        this.filePath = dto.filePath();

        update(user);
    }

    public void deactivate(User user){
        this.status = NotaFiscalStatus.INATIVA;

        update(user);
    }

    public void update(User user){
        this.updateDatetime = LocalDateTime.now();
        this.updateUser = user;
    }

}
