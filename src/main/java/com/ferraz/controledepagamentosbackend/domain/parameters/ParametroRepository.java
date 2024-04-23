package com.ferraz.controledepagamentosbackend.domain.parameters;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {
    List<Parametro> findAllByStatus(ParametroStatus parametroStatus);

}
