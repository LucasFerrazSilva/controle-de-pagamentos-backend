package com.ferraz.controledepagamentosbackend.domain.parameters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {


    @Query("SELECT p FROM Parametro p " +
            "WHERE (:nome IS NULL OR p.nome = :nome) " +
            "AND (:valor IS NULL OR p.valor = :valor) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Parametro> findByFiltros(
            Pageable pageable,
            @Param("nome") String nome,
            @Param("valor") String valor,
            @Param("status") ParametroStatus status
    );

}
