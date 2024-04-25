package com.ferraz.controledepagamentosbackend.domain.parameters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {
    Page<Parametro> findAllByStatus(Pageable pageable, ParametroStatus parametroStatus);

}
