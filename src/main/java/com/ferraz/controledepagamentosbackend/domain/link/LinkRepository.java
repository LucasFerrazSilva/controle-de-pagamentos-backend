package com.ferraz.controledepagamentosbackend.domain.link;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByLinkAndStatus(String link, LinkStatus status);

}
