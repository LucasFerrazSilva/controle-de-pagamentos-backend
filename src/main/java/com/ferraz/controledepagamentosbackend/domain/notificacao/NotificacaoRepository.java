package com.ferraz.controledepagamentosbackend.domain.notificacao;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUserAndStatusNot(User loggedUser, NotificacaoStatus status);

}
