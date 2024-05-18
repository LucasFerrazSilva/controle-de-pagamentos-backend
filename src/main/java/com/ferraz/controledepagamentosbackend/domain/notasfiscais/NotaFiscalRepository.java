package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

    @Query("SELECT nf FROM NotaFiscal nf " +
            "WHERE (:idUsuario is null or :idUsuario = nf.user.id) " +
            "AND (:mes IS NULL OR nf.mes = :mes) " +
            "AND (:ano IS NULL OR nf.ano = :ano) " +
            "AND (:valor IS NULL OR nf.valor = :valor) " +
            "AND (:status IS NULL OR nf.status = :status)")
    Page<NotaFiscal> findByFiltros(
            Pageable pageable,
            @Param("idUsuario") Long idUsuario,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            @Param("valor") BigDecimal valor,
            @Param("status") NotaFiscalStatus status
    );

    List<NotaFiscal> findByUser(User user);

}
