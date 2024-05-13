package com.ferraz.controledepagamentosbackend.domain.link;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByIdAndStatus(UUID id, LinkStatus status);

    Optional<Link> findByHorasExtrasIdAndAcao(Long idHorasExtras, AcaoLink acao);

}
