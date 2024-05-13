package com.ferraz.controledepagamentosbackend.domain.parameters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {


    @Query("SELECT p FROM Parametro p " +
            "WHERE (:nome IS NULL OR UPPER(p.nome) like CONCAT('%', UPPER(cast(:nome AS text)), '%')) " +
            "AND (:valor IS NULL OR UPPER(p.valor) like CONCAT('%', UPPER(cast(:valor AS text)), '%')) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Parametro> findByFiltros(
            Pageable pageable,
            @Param("nome") String nome,
            @Param("valor") String valor,
            @Param("status") ParametroStatus status
    );

}
