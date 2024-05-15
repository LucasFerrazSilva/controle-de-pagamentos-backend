package com.ferraz.controledepagamentosbackend.domain.notificacao;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Notificacao create(User user, String descricao, String path) {
        Notificacao notificacao = new Notificacao(user, descricao, path, AuthenticationService.getLoggedUser());
        return repository.save(notificacao);
    }
}
