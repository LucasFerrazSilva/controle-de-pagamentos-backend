package com.ferraz.controledepagamentosbackend.domain.link;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class LinkService {

    private LinkRepository repository;

    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Link criar(AcaoLink acao, HorasExtras horasExtras) {
        Link link = new Link(horasExtras, acao, getLoggedUser());
        return repository.save(link);
    }

    @Transactional
    public void inativarLinks(HorasExtras horasExtras) {
        List<Link> links = repository.findByHorasExtrasAndStatus(horasExtras, LinkStatus.CRIADO);
        links.forEach(link -> link.atualizar(LinkStatus.EXPIRADO, getLoggedUser()));
        repository.saveAll(links);
    }
}
